package com.ices.aigccommunity.utils;

import com.ices.aigccommunity.controller.content.vo.CommentVo;

import java.util.ArrayList;
import java.util.List;

public class MyTreeNode {
    private Long parentId;
    private Long Id;
    //子节点
    private List<MyTreeNode> commentVoList;
    //内容
    private CommentVo content;

    //创建节点

    public static MyTreeNode buildNode(CommentVo commentVo) {
        MyTreeNode node=new MyTreeNode();
        node.setId(commentVo.getId());
        node.setParentId(commentVo.getParentId());
        node.setContent(commentVo);
        return node;
    }
    public static MyTreeNode buildRoot() {
        MyTreeNode node=new MyTreeNode();
        node.setId(0L);
        node.setParentId(-999L);
        node.setContent(null);
        ArrayList<MyTreeNode> children=new ArrayList<>();
        node.setChildren(children);
        return node;
    }
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public List<MyTreeNode> getChildren() {
        return commentVoList;
    }

    public void setChildren(List<MyTreeNode> commentVoList) {
        this.commentVoList = commentVoList;
    }

    public CommentVo getContent() {
        return content;
    }

    public void setContent(CommentVo content) {
        this.content = content;
    }
}
