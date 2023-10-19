package com.ices.aigccommunity.controller.user;

import com.ices.aigccommunity.common.Constants;
import com.ices.aigccommunity.common.ServiceResultEnum;
import com.ices.aigccommunity.config.annotation.TokenToMallUser;
import com.ices.aigccommunity.controller.user.param.UserLoginParam;
import com.ices.aigccommunity.controller.user.param.UserRegisterParam;
import com.ices.aigccommunity.controller.user.vo.LoginVo;
import com.ices.aigccommunity.controller.user.vo.UserVo;
import com.ices.aigccommunity.enity.User;
import com.ices.aigccommunity.service.UserService;
import com.ices.aigccommunity.utils.Result;
import com.ices.aigccommunity.utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@CrossOrigin
public class UserController {

    @Resource
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/user/login")
    public Result login( @RequestBody @Valid UserLoginParam userLoginParam) {

        String loginResult = userService.login(userLoginParam.getLoginName(), userLoginParam.getPassword());

        logger.info("login api,loginName={},loginResult={}", userLoginParam.getLoginName(), loginResult);

        LoginVo loginVo=new LoginVo();

        //登录成功
        if (StringUtils.hasText(loginResult) && loginResult.length() == Constants.TOKEN_LENGTH) {
            Result result = ResultGenerator.genSuccessResult();
            User user=userService.getByLoginname(userLoginParam.getLoginName());
            loginVo.setUserId(user.getId());
            loginVo.setNickname(user.getEmail());
            loginVo.setAvatar(user.getAvatar());
            loginVo.setToken(loginResult);
            loginVo.setAuthority(user.getAuthority());

            result.setData(loginVo);
            return result;
        }
        //登录失败
        logger.info("登录失败");
        return ResultGenerator.genFailResult(loginResult);
    }

    @PostMapping("/user/register")
    public Result register(@RequestBody @Valid UserRegisterParam userRegisterParam) {

        String registerResult = userService.register(userRegisterParam.getLoginName(), userRegisterParam.getPassword(),userRegisterParam.getAvatar(),userRegisterParam.getNickname());

        logger.info("register api,loginName={},loginResult={}", userRegisterParam.getLoginName(), registerResult);

        //注册成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(registerResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //注册失败
        return ResultGenerator.genFailResult(registerResult);
    }

    @PostMapping("/user/logout")
    public Result logout(@TokenToMallUser @RequestBody User loginUser) {
        Boolean logoutResult = userService.logout(loginUser.getId());

        logger.info("logout api,loginMallUser={}", loginUser.getId());

        //登出成功
        if (logoutResult) {
            return ResultGenerator.genSuccessResult();
        }
        //登出失败
        return ResultGenerator.genFailResult("logout error");
    }

    @GetMapping("/user/info")
    public Result<UserVo> getUserDetail(@TokenToMallUser User loginUser) {
        //已登录则直接返回
        UserVo userVO = new UserVo();
        BeanUtils.copyProperties(loginUser, userVO);
        return ResultGenerator.genSuccessResult(userVO);
    }
}
