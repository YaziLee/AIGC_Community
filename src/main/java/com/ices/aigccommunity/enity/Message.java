package com.ices.aigccommunity.enity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Message {

    private Long id;
    private Long userId;
    private Long designId;
    private Long designImageId;
    private String imageUrl;
    private String comment;
    private Date publishTime;
    private Integer direction;
    private Integer readStatus;
}

