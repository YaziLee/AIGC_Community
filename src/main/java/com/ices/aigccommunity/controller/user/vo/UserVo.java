package com.ices.aigccommunity.controller.user.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserVo implements Serializable {

    private String email;

    private String avatar;

    private Long id;

    private String tel;


}
