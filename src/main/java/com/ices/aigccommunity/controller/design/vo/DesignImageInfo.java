package com.ices.aigccommunity.controller.design.vo;

import lombok.Data;

import java.util.List;

@Data
public  class DesignImageInfo {
    private Long id;
    private String url;
    private List<MarkInfo> mark;
}
