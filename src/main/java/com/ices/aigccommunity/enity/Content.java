package com.ices.aigccommunity.enity;

import lombok.Data;

import java.util.Date;

@Data
public class Content {
    private Long id;

    private Long publisher;

    private Long imageID;

    private Date publishTime;

    private Integer state;

    private String prompt;

    private String description;

    private Long browsed;

    private Long liked;

    private Long collected;
}
