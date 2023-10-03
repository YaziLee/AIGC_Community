package com.ices.aigccommunity.controller.content.param;

import lombok.Data;

@Data
public class CommentReplyParam {

    private long replyComemntID;

    private long userID;

    private String body;
}
