package com.ices.aigccommunity.service.impl;

import com.ices.aigccommunity.common.ServiceResultEnum;
import com.ices.aigccommunity.dao.CommentMapper;
import com.ices.aigccommunity.dao.ContentMapper;
import com.ices.aigccommunity.dao.ImageMapper;
import com.ices.aigccommunity.dao.LikedMapMapper;
import com.ices.aigccommunity.enity.Comment;
import com.ices.aigccommunity.enity.Content;
import com.ices.aigccommunity.enity.Image;
import com.ices.aigccommunity.enity.LikedMap;
import com.ices.aigccommunity.service.ContentService;
import com.ices.aigccommunity.utils.ImageUtil;
import com.ices.aigccommunity.utils.QiniuyunUtil;
import com.ices.aigccommunity.utils.Result;
import com.ices.aigccommunity.utils.ResultGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.io.File;
import java.util.Date;
import java.util.List;

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

    private static final Logger logger = LoggerFactory.getLogger(ContentServiceImpl.class);

    @Override
    @Transactional
    public String upload(String url1, String url2, String url3, String url4, String url5,String prompt,long publisherId){

        // 将封面图写入image表
        Image coverImage=new Image();
        coverImage.setUrl(url1);
        coverImage.setType(0);
        coverImage.setSubjectTo(0l);
        imageMapper.insertImage(coverImage);
        long coverImageID=coverImage.getId();
        System.out.println("插入封面图的主键ID是："+coverImage.getId());//注释

        // 将封面图写入content表
        Content content=new Content();
        //获得当前时间
        Date publishTime=new Date();
        logger.info("当前时间是："+publishTime);
        content.setPublishTime(publishTime);
        content.setPrompt(prompt);
        content.setPublisher(publisherId);
        content.setImageID(coverImageID);
        contentMapper.insertContent(content);
        long contentID=content.getId();
        //更新封面图的指向ID
        imageMapper.setFatherId(coverImageID,contentID);

        // 从七牛云上下载封面图
        File coverImageFile=QiniuyunUtil.downloadFile(url1);
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
            if(i==0&&url2!=null)
                insertImage(cropimageID,url2);
            if(i==1&&url3!=null)
                insertImage(cropimageID,url3);
            if(i==2&&url4!=null)
                insertImage(cropimageID,url4);
            if(i==3&&url5!=null)
                insertImage(cropimageID,url5);
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
    public Content getOne(long contentId){
        Content content=contentMapper.getById(contentId);
        contentMapper.updateBrowsed(contentId);
        return content;
    }

    @Transactional
    public Result liked(long userId, long contentId){

        likedMapMapper.insertRecord(userId,contentId);
        contentMapper.updateLiked(contentId);
        return ResultGenerator.genSuccessResult();

    }

    @Transactional
    public Result collected(long userId, long contentId){

        likedMapMapper.insertRecord(userId,contentId);
        contentMapper.updateLiked(contentId);
        return ResultGenerator.genSuccessResult();

    }



}
