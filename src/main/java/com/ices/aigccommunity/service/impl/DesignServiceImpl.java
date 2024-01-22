package com.ices.aigccommunity.service.impl;

import com.ices.aigccommunity.common.ServiceResultEnum;
import com.ices.aigccommunity.controller.design.DesignController;
import com.ices.aigccommunity.controller.design.param.DesignImageRequest;
import com.ices.aigccommunity.controller.design.param.DesignUploadParam;
import com.ices.aigccommunity.controller.design.param.MarkRequest;
import com.ices.aigccommunity.controller.design.vo.BasicInfo;
import com.ices.aigccommunity.controller.design.vo.DesignDetailVO;
import com.ices.aigccommunity.controller.design.vo.DesignImageInfo;
import com.ices.aigccommunity.controller.design.vo.MarkInfo;
import com.ices.aigccommunity.dao.DesignImageMapper;
import com.ices.aigccommunity.dao.DesignMapper;
import com.ices.aigccommunity.dao.MarkMapper;
import com.ices.aigccommunity.dao.MessageMapper;
import com.ices.aigccommunity.enity.*;
import com.ices.aigccommunity.service.DesignService;
import com.ices.aigccommunity.utils.QiniuyunUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DesignServiceImpl implements DesignService {
    @Resource
    DesignMapper designMapper;
    @Resource
    DesignImageMapper designImageMapper;
    @Resource
    MarkMapper markMapper;
    @Resource
    MessageMapper messageMapper;

    private static final Logger logger = LoggerFactory.getLogger(DesignServiceImpl.class);

    @Override
    @Transactional
    public String upload(DesignUploadParam designUploadParam,Long userID) {
        logger.info("designUploadParam参数是：{}",designUploadParam);

        //1 创建design对象为其赋值
        Design design=new Design();
        BeanUtils.copyProperties(designUploadParam,design);
        if(designUploadParam.getContentID()!=null){
            design.setDesignFromContentId(designUploadParam.getContentID());
        }
        design.setDesigner(userID);

        //2 将design对象插入design表中并获取返回Id值
        designMapper.insert(design);
        Long designID= design.getId();

        //3 将设计稿图依次插入design_images中，并获取其返回id值
        for(DesignImageRequest designImageRequest: designUploadParam.getDesignImages()){
            DesignImage designImage=new DesignImage();
            BeanUtils.copyProperties(designImageRequest,designImage);
            designImage.setDesignId(designID);
            designImage.setType(1);
            designImageMapper.insert(designImage);
            Long designImageID=designImage.getId();

            //4 将标注信息依次插入mark表中，并获取其返回id值
            for(MarkRequest markRequest: designImageRequest.getMark()){
                Mark mark=new Mark();
                BeanUtils.copyProperties(markRequest,mark);
                mark.setDesignId(designID);
                mark.setDesignImageId(designImageID);
                logger.info("插入的标记是{}",mark);
                markMapper.insert(mark);
                Long markID=mark.getId();

                //5 将标注面料图片依次插入design_images表中
                if(markRequest.getMaterialUrl()!=null){
                    DesignImage designImageMark=new DesignImage();
                    designImageMark.setUrl(markRequest.getMaterialUrl());
                    designImageMark.setType(2);
                    designImageMark.setMarkId(markID);
                    designImageMark.setDesignId(designID);
                    designImageMapper.insert(designImageMark);
                }
            }
        }
        return ServiceResultEnum.SUCCESS.getResult();
    }

    @Override
    public List getAll() {
        List<Design> designs=designMapper.selectAll();
        return designs;
    }

    @Override
    public DesignDetailVO getByID(Long id) {
        DesignDetailVO designDetailVO=new DesignDetailVO();

        //1 根据id在design表中查询到基本信息
        Design design=designMapper.selectById(id);
        designDetailVO.setBasic(new BasicInfo());
        BeanUtils.copyProperties(design,designDetailVO.getBasic());
        designDetailVO.getBasic().setDesignerID(design.getDesigner());
        designDetailVO.getBasic().setContentID(design.getDesignFromContentId());

        //2 根据id在design_images表中查询到所有设计稿图
        List<DesignImage> designImages=designImageMapper.selectByDesignId(id);

        //3 根据设计稿图id查询所有标注信息
        List<DesignImageInfo> designImageInfos=new LinkedList<>();
        for(DesignImage designImage:designImages){
            DesignImageInfo designImageInfo=new DesignImageInfo();
            BeanUtils.copyProperties(designImage,designImageInfo);
            List<Mark> marks=markMapper.selectByDesignImageId(designImage.getId());
            List<MarkInfo> markInfos=new LinkedList<>();
            for(Mark mark:marks){
                MarkInfo markInfo=new MarkInfo();
                BeanUtils.copyProperties(mark,markInfo);
                markInfo.setMaterialUrl(designImageMapper.selectMarkDesignImageByID(mark.getId()).getUrl());
                markInfos.add(markInfo);
            }
            designImageInfo.setMark(markInfos);
            designImageInfos.add(designImageInfo);
        }

        //4 拿到上面的信息封装为一个对象返回给前端
        designDetailVO.setDesignImages(designImageInfos);
        return designDetailVO;
    }

    @Override
    public List getByContent(Long contentID){
        List<Design> designs=designMapper.selectByDesignFromContentId(contentID);
        return designs;
    }

    @Override
    public List getByDesigner(Long designerID){
        List<Design> designs=designMapper.getByDesigner(designerID);
        return designs;
    }

    @Override
    @Transactional
    public String deleteByID(Long id){
        //查询设计款的所有图片，包括设计稿图、面料图、建议图
        List<String> urls=new ArrayList<>();
        List<DesignImage> designImages=designImageMapper.selectByDesignId(id);
        for(DesignImage designImage:designImages){
            urls.add(designImage.getUrl());
        }
        List<Message> messages=messageMapper.selectByDesignId(id);
        for(Message message:messages){
            urls.add(message.getImageUrl());
        }
        //提取七牛云图片uuid
        List<String> imageKey=new ArrayList<>();
        Pattern pattern = Pattern.compile("/([0-9a-fA-F-]+)$");
        for(String url:urls){
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                imageKey.add( matcher.group(1));
            }
        }
        //删除七牛云图片对象
        String[] imageUrlArray = new String[imageKey.size()];
        imageKey.toArray(imageUrlArray);
        logger.info("设计款{}的图片url为：{}",id,imageUrlArray);
        List<String> results= QiniuyunUtil.deleteImage(imageUrlArray);
        logger.info("设计款{}的七牛云图片删除结果为：{}",id,results);

        //删除设计款相关的所有标注
        markMapper.deleteByDesignId(id);
        //删除设计款的所有设计稿
        designImageMapper.deleteByDesignId(id);
        //删除设计款的所有建议消息
        messageMapper.deleteByDesignId(id);
        //最后删除设计款
        designMapper.deleteById(id);

        return ServiceResultEnum.SUCCESS.getResult();
    }
}
