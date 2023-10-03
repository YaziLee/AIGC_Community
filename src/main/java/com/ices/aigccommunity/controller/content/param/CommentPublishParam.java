package com.ices.aigccommunity.controller.content.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentPublishParam implements Serializable {

    private long contentID;

    private long userID;

    private String body;

    private int level;

    private long toUid;

    private long parentId;
}
