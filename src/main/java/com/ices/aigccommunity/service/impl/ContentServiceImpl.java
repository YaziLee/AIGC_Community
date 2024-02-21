package com.ices.aigccommunity.service.impl;

import com.ices.aigccommunity.common.ServiceResultEnum;
import com.ices.aigccommunity.controller.content.param.ContentUploadParam;
import com.ices.aigccommunity.controller.content.vo.ContentDetailVO;
import com.ices.aigccommunity.dao.*;
import com.ices.aigccommunity.enity.*;
import com.ices.aigccommunity.service.ContentService;
import com.ices.aigccommunity.utils.ImageUtil;
import com.ices.aigccommunity.utils.QiniuyunUtil;
import com.ices.aigccommunity.utils.Result;
import com.ices.aigccommunity.utils.ResultGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ContentServiceImpl implements ContentService {

    @Resource
    ContentMapper contentMapper;
    @Resource
    ImageMapper imageMapper;
    @Resource
    LikedMapMapper likedMapMapper;
    @Resource
    CollectedMapMapper collectedMapMapper;
    @Resource
    CommentMapper commentMapper;

    private static final Logger logger = LoggerFactory.getLogger(ContentServiceImpl.class);

    @Override
    @Transactional
    public String upload(ContentUploadParam contentUploadParam){

        // 将封面图写入image表
        Image coverImage=new Image();
        coverImage.setUrl(contentUploadParam.getImageUrl1());
        coverImage.setType(0);
        coverImage.setSubjectTo(0l);
        imageMapper.insertImage(coverImage);
        long coverImageID=coverImage.getId();

        // 将封面图写入content表
        Content content=new Content();
        content.setPrompt(contentUploadParam.getPrompt());
        content.setPublisher(contentUploadParam.getPublisherId());
        content.setImageID(coverImageID);
        content.setDescription(contentUploadParam.getDescription());
        content.setName(contentUploadParam.getName());
        contentMapper.insertContent(content);
        long contentID=content.getId();
        //更新封面图的指向ID
        imageMapper.setFatherId(coverImageID,contentID);

        //将真值图插入image表
        for(String url:contentUploadParam.getRealImages()){
            Image realImage=new Image();
            realImage.setType(4);
            realImage.setUrl(url);
            realImage.setSubjectTo(contentID);
            imageMapper.insertImage(realImage);
        }

        // 从七牛云上下载封面图
        File coverImageFile=QiniuyunUtil.downloadFile(contentUploadParam.getImageUrl1());
        //裁剪封面图
        List<File> cropedImageFiles=ImageUtil.imageCrop(coverImageFile);
        //剪裁完成，删除封面图
        coverImageFile.delete();

        // 将裁剪的图片依次上传七牛云，并写入image表
        int i=0;
        for(File cropedImageFile:cropedImageFiles){
            //将图片上传七牛云
            String cropedImageUrl=QiniuyunUtil.uploadFile(cropedImageFile);
            //上传完成，将本地裁剪图片删除
            cropedImageFile.delete();

            //将裁剪图写入image表
            Image imageCrop=new Image();
            imageCrop.setSubjectTo(coverImageID);
            imageCrop.setUrl(cropedImageUrl);
            imageCrop.setType(1);
            imageMapper.insertImage(imageCrop);
            long cropimageID =imageCrop.getId();
            System.out.println("插入裁剪图的ID是："+cropimageID);//注释

            //依次上传衍生图片
            if(i==0&&contentUploadParam.getImageUrl2()!=null)
                insertImage(cropimageID,contentUploadParam.getImageUrl2());
            if(i==1&&contentUploadParam.getImageUrl3()!=null)
                insertImage(cropimageID,contentUploadParam.getImageUrl3());
            if(i==2&&contentUploadParam.getImageUrl4()!=null)
                insertImage(cropimageID,contentUploadParam.getImageUrl4());
            if(i==3&&contentUploadParam.getImageUrl5()!=null)
                insertImage(cropimageID,contentUploadParam.getImageUrl5());
            //变量i用来记录四宫格位置
            i++;
        }

        return ServiceResultEnum.SUCCESS.getResult();
    }

    public void insertImage(long fatherImageId,String newImageUrl){
        // 将衍生图四宫格写入image表
        Image newImage=new Image();
        newImage.setUrl(newImageUrl);
        newImage.setType(1);
        newImage.setSubjectTo(fatherImageId);
        imageMapper.insertImage(newImage);
        long newImageID=newImage.getId();
        System.out.println("插入衍生图图的ID是："+newImageID);//注释

        // 从七牛云上下载衍生图
        File newImageFile=QiniuyunUtil.downloadFile(newImageUrl);
        // 裁剪衍生图
        List<File> newImageCropFiles=ImageUtil.imageCrop(newImageFile);
        //删除衍生图
        newImageFile.delete();

        //将衍生图裁剪的图片依次上传七牛云，并写入image表
        for(File newImageCropFile:newImageCropFiles) {
            //将衍生图的裁剪图上传七牛云
            String newImageCropUrl = QiniuyunUtil.uploadFile(newImageCropFile);
            //删除本地的衍生图的裁剪图
            newImageCropFile.delete();

            //将裁剪图写入image表
            Image newImageCrop = new Image();
            newImageCrop.setSubjectTo(newImageID);
            newImageCrop.setUrl(newImageCropUrl);
            newImageCrop.setType(1);
            imageMapper.insertImage(newImageCrop);
        }
    }

    public List<Content> getALL(){
        List<Content> allContent=contentMapper.getAllContent();
        return allContent;
    }

    @Transactional
    public ContentDetailVO getDetail(long userId, long contentId){
        Content content=contentMapper.getById(contentId);
        ContentDetailVO contentDetailVO=new ContentDetailVO();
        BeanUtils.copyProperties(content,contentDetailVO);

        LikedMap likedMap=likedMapMapper.selectByContentUserId(userId,contentId);
        CollectedMap collectedMap=collectedMapMapper.selectByContentUserId(userId,contentId);

        if(likedMap==null)
            contentDetailVO.setCollectedd(false);
        else
            contentDetailVO.setLikedd(true);
        if(collectedMap==null)
            contentDetailVO.setCollectedd(false);
        else
            contentDetailVO.setCollectedd(true);
        contentMapper.updateBrowsed(contentId);
        return contentDetailVO;
    }

    @Transactional
    public Result liked(long userId, long contentId){

        likedMapMapper.insertRecord(userId,contentId);
        contentMapper.incLiked(contentId);
        return ResultGenerator.genSuccessResult();

    }

    @Transactional
    public Result collected(long userId, long contentId){

        collectedMapMapper.insertRecord(userId,contentId);
        contentMapper.incCollected(contentId);
        return ResultGenerator.genSuccessResult();

    }

    @Transactional
    public Result disLiked(long userId, long contentId){

        likedMapMapper.deleteRecord(userId,contentId);
        contentMapper.decLiked(contentId);
        return ResultGenerator.genSuccessResult();

    }

    @Transactional
    public Result disCollected(long userId, long contentId){

        collectedMapMapper.deleteRecord(userId,contentId);
        contentMapper.decCollected(contentId);
        return ResultGenerator.genSuccessResult();

    }
    @Transactional
    public String deleteByID(int id){
        //查询内容
        Content content=contentMapper.getById(id);
        //得到封面图
        Image coverImage=imageMapper.getById(content.getImageID());
        //创建待删除的图片对象列表
        List<Image> images=new ArrayList<>();
        images.add(coverImage);
        //根据树形结构依次获取内容所属图片并加入待删除列表
        List<Image> coverCropImages=imageMapper.getSon(coverImage.getId());
        for(Image coverCropImage:coverCropImages){
            images.add(coverCropImage);
            List<Image> newImages=imageMapper.getSon(coverCropImage.getId());
            for(Image newImage:newImages){
                images.add(newImage);
                List<Image> newCropImages=imageMapper.getSon(newImage.getId());
                for(Image newCropImage:newCropImages){
                    images.add(newCropImage);
                }
            }
        }
        //删除图片记录
        List<String> imageKey=new ArrayList<>();
        //提取七牛云图片uuid
        Pattern pattern = Pattern.compile("/([0-9a-fA-F-]+)$");
        for(Image image:images){
            Matcher matcher = pattern.matcher(image.getUrl());
            if (matcher.find()) {
                imageKey.add( matcher.group(1));
            }
            imageMapper.deleteByID(image.getId());
        }
        //删除七牛云图片对象
        String[] imageUrlArray = new String[imageKey.size()];
        imageKey.toArray(imageUrlArray);
        logger.info("内容{}的图片url为：{}",content.getId(),imageUrlArray);
        List<String> results=QiniuyunUtil.deleteImage(imageUrlArray);
        logger.info("内容{}的七牛云图片删除结果为：{}",content.getId(),results);
        //删除评论、点赞、收藏
        commentMapper.deleteByContentID(content.getId());
        likedMapMapper.deleteByContentID(content.getId());
        collectedMapMapper.deleteByContentID(content.getId());
        contentMapper.deleteByID(id);

        return ServiceResultEnum.SUCCESS.getResult();
    }



}
