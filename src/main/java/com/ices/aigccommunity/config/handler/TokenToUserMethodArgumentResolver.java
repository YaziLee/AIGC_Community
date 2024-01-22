package com.ices.aigccommunity.config.handler;

import com.ices.aigccommunity.common.AigcCommunityException;
import com.ices.aigccommunity.common.Constants;
import com.ices.aigccommunity.common.ServiceResultEnum;
import com.ices.aigccommunity.config.annotation.TokenToUser;
import com.ices.aigccommunity.dao.UserMapper;
import com.ices.aigccommunity.dao.UserTokenMapper;
import com.ices.aigccommunity.enity.User;
import com.ices.aigccommunity.enity.UserToken;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class TokenToUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserTokenMapper userTokenMapper;

    public TokenToUserMethodArgumentResolver() {
    }

    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(TokenToUser.class)) {
            return true;
        }
        return false;
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (parameter.getParameterAnnotation(TokenToUser.class) instanceof TokenToUser) {
            User user = null;
            String token = webRequest.getHeader("token");
            //当token不为空时也就是处于登录状态
            if (null != token && !"".equals(token) && token.length() == Constants.TOKEN_LENGTH) {
                //根据token查找数据库
                UserToken mallUserToken = userTokenMapper.selectByToken(token);
                //当token已经过期则报错
                if (mallUserToken == null || mallUserToken.getExpireTime().getTime() <= System.currentTimeMillis()) {
                    AigcCommunityException.fail(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
                }
                //根据token查询用户
                user = userMapper.getById(mallUserToken.getUserId());
                //如果为空则报错，否则返回用户实例
                if (user == null) {
                    AigcCommunityException.fail(ServiceResultEnum.USER_NULL_ERROR.getResult());
                }
                return user;
            }
            //如果token为空则报错，禁止访问接口
            else {
                AigcCommunityException.fail(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            }
        }
        return null;
    }

    public static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

}
