package com.ices.aigccommunity.controller.user.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class UserRegisterParam implements Serializable {

    @NotEmpty(message = "登录名不能为空")
    private String loginName;

    @NotEmpty(message = "密码不能为空")
    private String password;

    private String nickname;

    private String avatar;
}
