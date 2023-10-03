package com.ices.aigccommunity.controller.content.vo;

import com.ices.aigccommunity.controller.user.vo.UserVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CommentVo implements Serializable {
    private Long id;

    private UserVo author;

    private String body;

    private List<CommentVo> childrens;

    private String replyTime;

    private Integer level;

    private UserVo toUser;
    //父节点id
    private Long parentId;
}

