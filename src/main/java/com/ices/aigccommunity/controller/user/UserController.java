package com.ices.aigccommunity.controller.user;

import com.ices.aigccommunity.common.Constants;
import com.ices.aigccommunity.common.ServiceResultEnum;
import com.ices.aigccommunity.config.annotation.TokenToMallUser;
import com.ices.aigccommunity.controller.user.param.UserLoginParam;
import com.ices.aigccommunity.controller.user.param.UserRegisterParam;
import com.ices.aigccommunity.enity.User;
import com.ices.aigccommunity.service.UserService;
import com.ices.aigccommunity.utils.Result;
import com.ices.aigccommunity.utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
public class UserController {

    @Resource
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/user/login")
    public Result<String> login( @RequestBody @Valid UserLoginParam userLoginParam) {

        String loginResult = userService.login(userLoginParam.getLoginName(), userLoginParam.getPassword());

        logger.info("login api,loginName={},loginResult={}", userLoginParam.getLoginName(), loginResult);

        //登录成功
        if (StringUtils.hasText(loginResult) && loginResult.length() == Constants.TOKEN_LENGTH) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(loginResult);
            return result;
        }
        //登录失败
        logger.info("登录失败");
        return ResultGenerator.genFailResult(loginResult);
    }

    @PostMapping("/user/register")
    public Result register(@RequestBody @Valid UserRegisterParam userRegisterParam) {

        String registerResult = userService.register(userRegisterParam.getLoginName(), userRegisterParam.getPassword());

        logger.info("register api,loginName={},loginResult={}", userRegisterParam.getLoginName(), registerResult);

        //注册成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(registerResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //注册失败
        return ResultGenerator.genFailResult(registerResult);
    }

    @PostMapping("/user/logout")
    public Result<String> logout(@TokenToMallUser @RequestBody User loginUser) {
        Boolean logoutResult = userService.logout(loginUser.getId());

        logger.info("logout api,loginMallUser={}", loginUser.getId());

        //登出成功
        if (logoutResult) {
            return ResultGenerator.genSuccessResult();
        }
        //登出失败
        return ResultGenerator.genFailResult("logout error");
    }

}
