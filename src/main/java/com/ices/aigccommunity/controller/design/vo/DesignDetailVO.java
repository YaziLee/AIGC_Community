package com.ices.aigccommunity.controller.design.vo;

import lombok.Data;

import java.util.List;

@Data
public class DesignDetailVO {
    private BasicInfo basic;
    private List<DesignImageInfo> designImages;
}
