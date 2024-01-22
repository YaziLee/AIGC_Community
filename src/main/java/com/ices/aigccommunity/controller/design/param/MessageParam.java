package com.ices.aigccommunity.controller.design.param;

import lombok.Data;

@Data
public class MessageParam {
    private Long designId;
    private Long designImageId;
    private String imageUrl;
    private String comment;
}
