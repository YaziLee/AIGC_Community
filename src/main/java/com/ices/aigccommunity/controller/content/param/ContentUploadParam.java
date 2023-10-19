package com.ices.aigccommunity.controller.content.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ContentUploadParam implements Serializable {



    @NotEmpty(message = "封面图不能为空")
    private String imageUrl1;

    private String imageUrl2;
    private String imageUrl3;
    private String imageUrl4;
    private String imageUrl5;

    @NotEmpty(message = "提示词不能为空")
    private String prompt;

    @NotNull(message = "发布者id不能为空")
    private Long publisherId;

    private String name;

    private String description;
}
