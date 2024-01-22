package com.ices.aigccommunity.controller.content;

import com.ices.aigccommunity.common.ServiceResultEnum;
import com.ices.aigccommunity.controller.content.param.ContentUploadParam;
import com.ices.aigccommunity.controller.content.param.GetDetailParam;
import com.ices.aigccommunity.controller.content.vo.ContentDetailVO;
import com.ices.aigccommunity.dao.CollectedMapMapper;
import com.ices.aigccommunity.dao.LikedMapMapper;
import com.ices.aigccommunity.enity.CollectedMap;
import com.ices.aigccommunity.enity.Content;
import com.ices.aigccommunity.enity.LikedMap;
import com.ices.aigccommunity.service.ContentService;
import com.ices.aigccommunity.service.ImageService;
import com.ices.aigccommunity.utils.Result;
import com.ices.aigccommunity.utils.ResultGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ContentController {

    @Resource
    ContentService contentService;
    @Resource
    ImageService imageService;
    @PostMapping("/content/upload")
    public Result<String> upload(@RequestBody @Valid ContentUploadParam contentUploadParam) {

        //1 根据URL去七牛云下载图片
        //2 对图片进行切分，将切分图片上传七牛云
        //3 进行上传
        //3.1 将上传内容插入content表
        //3.2 将图片及隶属关系插入image表
        //  插入算法：用五个列表，分别记录五张四宫格图片及其切分图片的URL（列表长度为5）
        //4.上传成功返回

        String uploadResult=contentService.upload(contentUploadParam);
        Result result = ResultGenerator.genSuccessResult();
        result.setData(uploadResult);
        return result;
    }

    @GetMapping("/content/getAll")
    public Result getAll() {
        List<Content> contents=contentService.getALL();
        List<ContentDetailVO> contentDetailVOList=new ArrayList<>();
        for(Content content:contents){
            ContentDetailVO contentDetailVO=new ContentDetailVO();
            BeanUtils.copyProperties(content,contentDetailVO);
            contentDetailVO.setImageURL(imageService.getOne(content.getImageID()).getUrl());
            contentDetailVOList.add(contentDetailVO);
        }
        Result result = ResultGenerator.genSuccessResult(contentDetailVOList);
        return result;
    }

    @PostMapping("/content/getDetail")
    public Result<String> getDetail(@RequestBody GetDetailParam getDetailParam) {
        ContentDetailVO contentDetailVO=contentService.getDetail(getDetailParam.getUserId(),getDetailParam.getContentId());

        Result result = ResultGenerator.genSuccessResult(contentDetailVO);
        return result;
    }

    @GetMapping("/content/liked")
    public Result liked(long userId,long contentId){
        return contentService.liked(userId,contentId);
    }

    @GetMapping("/content/collected")
    public Result collected(long userId,long contentId){
        return contentService.collected(userId,contentId);
    }

    @GetMapping("/content/disLiked")
    public Result disLiked(long userId,long contentId){
        return contentService.disLiked(userId,contentId);
    }

    @GetMapping("/content/disCollected")
    public Result disCollected(long userId,long contentId){
        return contentService.disCollected(userId,contentId);
    }

    @GetMapping("/content/deleteByID")
    public Result deleteByID(int id) {
        contentService.deleteByID(id);
        Result result = ResultGenerator.genSuccessResult();
        return result;
    }



}
