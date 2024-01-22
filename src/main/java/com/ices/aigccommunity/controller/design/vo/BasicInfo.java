package com.ices.aigccommunity.controller.design.vo;

import lombok.Data;

@Data
public class BasicInfo {
    private Long designerID;
    private Long contentID;
    private String name;
    private String description;
    private Float price;
    private String size;
    private Integer time;
    private String contact;
}
