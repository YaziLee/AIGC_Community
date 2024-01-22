package com.ices.aigccommunity.enity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Design {

    private Long id;
    private Long designer;
    private Long designFromContentId;
    private String name;
    private String description;
    private Float price;
    private String size;
    private Integer time;
    private String contact;
    private Date publishTime;
}
