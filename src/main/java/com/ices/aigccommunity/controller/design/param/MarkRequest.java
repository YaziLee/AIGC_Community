package com.ices.aigccommunity.controller.design.param;

import lombok.Data;

@Data
public class MarkRequest {
    private Integer type;
    private String name;
    private String description;
    private String region;
    private String materialUrl;
}
