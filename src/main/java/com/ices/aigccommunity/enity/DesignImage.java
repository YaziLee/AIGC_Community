package com.ices.aigccommunity.enity;

import lombok.Data;

@Data
public class DesignImage {

    private Long id;
    private String url;
    private Integer type;
    private Long designId;
    private Long markId;
}
