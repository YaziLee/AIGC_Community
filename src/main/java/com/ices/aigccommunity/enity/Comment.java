package com.ices.aigccommunity.enity;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private long id;

    private int level;

    private long parentId;

    private Date replyTime;

    private long userId;

    private String body;

    private long toUid;

    private long contentId;
}
