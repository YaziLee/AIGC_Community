package com.ices.aigccommunity.controller.design.param;

import lombok.Data;

import java.util.List;

@Data
public class DesignImageRequest {
    private String url;
    private Integer type;
    private List<MarkRequest> mark;
}