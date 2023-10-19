package com.ices.aigccommunity.controller.content.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class GetDetailParam implements Serializable {
    @NotEmpty(message = "内容id不能为空")
    private int contentId;

    @NotEmpty(message = "用户id不能为空")
    private int userId;


}
