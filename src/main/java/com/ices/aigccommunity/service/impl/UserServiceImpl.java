package com.ices.aigccommunity.service.impl;

import com.ices.aigccommunity.common.ServiceResultEnum;
import com.ices.aigccommunity.controller.content.CommentController;
import com.ices.aigccommunity.dao.UserMapper;
import com.ices.aigccommunity.dao.UserTokenMapper;
import com.ices.aigccommunity.enity.User;
import com.ices.aigccommunity.enity.UserToken;
import com.ices.aigccommunity.service.UserService;

import com.ices.aigccommunity.utils.MD5Util;

import com.ices.aigccommunity.utils.NumberUtil;
import com.ices.aigccommunity.utils.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;


@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserTokenMapper userTokenMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public String register(String loginName, String password) {
        //验证有无同名用户

        User registerUser = new User();
        registerUser.setUsername(loginName);

        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
        registerUser.setPassword(passwordMD5);

        if (userMapper.register(registerUser) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String login(String loginName, String passwordMD5) {
        User user = userMapper.login(loginName, passwordMD5);
        if (user != null) {

            //登录后即执行修改token的操作
            String token = getNewToken(System.currentTimeMillis() + "", user.getId());
            UserToken mallUserToken = userTokenMapper.selectByPrimaryKey(user.getId());
            //当前时间
            Date now = new Date();
            //过期时间
            Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000);//过期时间 48 小时
            if (mallUserToken == null) {
                mallUserToken = new UserToken();
                mallUserToken.setUserId(user.getId());
                mallUserToken.setToken(token);
                mallUserToken.setUpdateTime(now);
                mallUserToken.setExpireTime(expireTime);
                //新增一条token数据
                if (userTokenMapper.insertSelective(mallUserToken) > 0) {
                    //新增成功后返回
                    return token;
                }
            } else {
                mallUserToken.setToken(token);
                mallUserToken.setUpdateTime(now);
                mallUserToken.setExpireTime(expireTime);
                //更新
                if (userTokenMapper.updateByPrimaryKeySelective(mallUserToken) > 0) {
                    //修改成功后返回
                    return token;
                }
            }
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    private String getNewToken(String timeStr, Long userId) {
        String src = timeStr + userId + NumberUtil.genRandomNum(4);
        return SystemUtil.genToken(src);
    }

    public Boolean logout(Long userId) {
        return userTokenMapper.deleteByPrimaryKey(userId) > 0;
    }

    public User getById(long id){
        User user=userMapper.getById(id);
        return user;
    }

}
