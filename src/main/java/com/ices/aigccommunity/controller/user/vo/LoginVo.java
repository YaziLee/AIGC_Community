package com.ices.aigccommunity.controller.user.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginVo implements Serializable {
    String token;
    Long userId;
    String nickname;
    String avatar;
    int authority;
}
