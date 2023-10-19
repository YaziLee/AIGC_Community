package com.ices.aigccommunity.controller.content;

import com.ices.aigccommunity.controller.content.param.CommentPublishParam;
import com.ices.aigccommunity.controller.content.param.CommentReplyParam;
import com.ices.aigccommunity.service.CommentService;
import com.ices.aigccommunity.service.impl.ContentServiceImpl;
import com.ices.aigccommunity.utils.Result;
import com.ices.aigccommunity.utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin
public class CommentController {
    @Resource
    CommentService commentService;

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @PostMapping("comment/publish")
    public Result publish(@RequestBody CommentPublishParam commentPublishParam){
        logger.info("请求参数：{}",commentPublishParam.getUserID());
        System.out.println("请求参数："+commentPublishParam);
        commentService.publish(commentPublishParam);
        return ResultGenerator.genSuccessResult();
    }


    @GetMapping("/comment/getByContent")
    public Result getCommentsById(Long id){
        logger.info("请求参数：{}",id);
        return commentService.getCommentsById(id);
    }

}
