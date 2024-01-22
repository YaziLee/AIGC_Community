package com.ices.aigccommunity.controller.design.param;
import lombok.Data;

import java.util.List;

@Data
public class DesignUploadParam {
    private Long contentID;
    private String name;
    private String description;
    private Float price;
    private String size;
    private Integer time;
    private String contact;
    private List<DesignImageRequest> designImages;

}
