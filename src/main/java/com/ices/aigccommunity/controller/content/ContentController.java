package com.ices.aigccommunity.controller.content;

import com.ices.aigccommunity.common.ServiceResultEnum;
import com.ices.aigccommunity.controller.content.param.ContentUploadParam;
import com.ices.aigccommunity.controller.content.param.GetDetailParam;
import com.ices.aigccommunity.enity.Content;
import com.ices.aigccommunity.service.ContentService;
import com.ices.aigccommunity.utils.Result;
import com.ices.aigccommunity.utils.ResultGenerator;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
public class ContentController {

    @Resource
    ContentService contentService;

    @PostMapping("/content/upload")
    public Result<String> upload(@RequestBody @Valid ContentUploadParam contentUploadParam) {

        //1 根据URL去七牛云下载图片
        //2 对图片进行切分，将切分图片上传七牛云
        //3 进行上传
        //3.1 将上传内容插入content表
        //3.2 将图片及隶属关系插入image表
        //  插入算法：用五个列表，分别记录五张四宫格图片及其切分图片的URL（列表长度为5）
        //4.上传成功返回

        String uploadResult=contentService.upload(contentUploadParam.getImageUrl1(),contentUploadParam.getImageUrl2(),contentUploadParam.getImageUrl3(),contentUploadParam.getImageUrl4(),contentUploadParam.getImageUrl5(),contentUploadParam.getPrompt(),contentUploadParam.getPublisherId());
        Result result = ResultGenerator.genSuccessResult();
        result.setData(uploadResult);
        return result;
    }

    @GetMapping("/content/getAll")
    public Result<String> getAll() {
        List<Content> contents=contentService.getALL();
        Result result = ResultGenerator.genSuccessResult(contents);
        return result;
    }

    @PostMapping("/content/getDetail")
    public Result<String> getDetail(@RequestBody GetDetailParam getDetailParam) {
        Content content=contentService.getOne(getDetailParam.getContentId());
        Result result = ResultGenerator.genSuccessResult(content);
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

}
