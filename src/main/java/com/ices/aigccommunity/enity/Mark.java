package com.ices.aigccommunity.enity;

import lombok.Data;

@Data
public class Mark {

    private Long id;
    private Long designId;
    private Long designImageId;
    private Integer type;
    private String name;
    private String description;
    private String region;
}