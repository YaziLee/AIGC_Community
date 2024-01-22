package com.ices.aigccommunity.controller.design;

import com.ices.aigccommunity.common.ServiceResultEnum;
import com.ices.aigccommunity.config.annotation.TokenToUser;
import com.ices.aigccommunity.controller.design.param.DesignUploadParam;
import com.ices.aigccommunity.controller.design.vo.DesignDetailVO;
import com.ices.aigccommunity.enity.Design;
import com.ices.aigccommunity.enity.User;
import com.ices.aigccommunity.service.DesignService;
import com.ices.aigccommunity.service.UserService;
import com.ices.aigccommunity.utils.Result;
import com.ices.aigccommunity.utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/api")
public class DesignController {

    private static final Logger logger = LoggerFactory.getLogger(DesignController.class);

    @Resource
    DesignService designService;


    //关于@TokenToUser用法的简单解释：它会为注解字段自动赋值，找到请求头里token对应的用户
    @PostMapping("/design/upload")
    public Result upload(@RequestBody DesignUploadParam designUploadParam, @TokenToUser User loginUser) {
        logger.info("设计师loginUser:{},请求上传设计款", loginUser.toString());
        String result=designService.upload(designUploadParam,loginUser.getId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    @GetMapping("/design/getAll")
    public Result getAll() {
        List<Design> designs=designService.getAll();
        return ResultGenerator.genSuccessResult(designs);
    }

    @GetMapping("/design/getByID")
    public Result getByID(Long id) {
        DesignDetailVO designDetailVO=designService.getByID(id);
        return ResultGenerator.genSuccessResult(designDetailVO);
    }

    @GetMapping("/design/getByContent")
    public Result getByContent(Long contentID){
        List<Design> designs=designService.getByContent(contentID);
        return ResultGenerator.genSuccessResult(designs);
    }

    @GetMapping("/design/deleteByID")
    public Result deleteByID(Long id, @TokenToUser User loginUser){
        logger.info("设计师loginUser:{},请求删除设计款", loginUser.toString());
        String result=designService.deleteByID(id);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    @GetMapping("/design/getMyDesigns")
    public Result getMyDesigns(Long designID){
        List<Design> designs= designService.getByDesigner(designID);
        return ResultGenerator.genSuccessResult(designs);
    }

}
