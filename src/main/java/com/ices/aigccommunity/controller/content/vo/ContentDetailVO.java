package com.ices.aigccommunity.controller.content.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ContentDetailVO implements Serializable {
    private Long id;

    private Long publisher;

    private Long imageID;

    private String imageURL;

    private Date publishTime;

    private Integer state;

    private String prompt;

    private String description;

    private String name;

    private Long browsed;

    private Long liked;

    private Long collected;

    private boolean isCollectedd;

    private boolean isLikedd;
}
