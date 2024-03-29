package com.ices.aigccommunity.controller.content;

import com.ices.aigccommunity.service.UploadTokenService;
import com.ices.aigccommunity.utils.Result;
import com.ices.aigccommunity.utils.ResultGenerator;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class QiniuyunUploadTokenController {

    @Resource
    private UploadTokenService uploadTokenService;

    @GetMapping("/qiniuyun/uploadToken")
    public Result getUploadToken(boolean forever){
        return ResultGenerator.genSuccessResult((Object)uploadTokenService.getUploadToken(forever));
    }
}
