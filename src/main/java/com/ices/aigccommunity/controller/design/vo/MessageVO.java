package com.ices.aigccommunity.controller.design.vo;

import lombok.Data;

import java.util.Date;

@Data
public  class MessageVO {
    private Long designImageId;
    private String imageUrl;
    private String comment;
    private Date publishTime;
    private int readStatus;
}
