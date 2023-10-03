package com.ices.aigccommunity.utils;

import com.ices.aigccommunity.controller.content.vo.CommentVo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//实现多级评论，将此篇文章的全部评论封装成为一棵树
@Component
public class TreeUtils {

    public  MyTreeNode buildTree(List<CommentVo> commentVoList){
        //创建根节点 id为0
        MyTreeNode root = MyTreeNode.buildRoot();
        //根节点的子节点列表
        List<MyTreeNode> rootChildren=new ArrayList<>();
        //将评论封装为树的节点
        List<MyTreeNode> nodes=new ArrayList<>();
        for (CommentVo commentVo : commentVoList) {
            MyTreeNode TreeNode = MyTreeNode.buildNode(commentVo);
            nodes.add(TreeNode);
        }
        for (MyTreeNode node : nodes) {
            MyTreeNode parentNode=findParentNode(node,nodes);
            if(parentNode!=null){
                //不为空，说明有父节点
                if(parentNode.getChildren()==null){
                    //说明这个节点是父节点的第一个子节点，创建一个子节点数组
                    List<MyTreeNode> children=new ArrayList<>();
                    children.add(node);
                    parentNode.setChildren(children);
                }else{
                    parentNode.getChildren().add(node);
                }
            }else{
                //为空，说明父节点为根节点
                //父节点添加子节点
                rootChildren.add(node);
                //子节点添加父节点id
                node.setParentId(root.getId());
            }
            root.setChildren(rootChildren);
        }

        return root;
    }
    //找到子节点的父节点
    public MyTreeNode findParentNode(MyTreeNode childNode,List<MyTreeNode> allNodes){
        for (MyTreeNode node : allNodes) {
            if(childNode.getParentId().equals(node.getId())){
                //将子评论的内容添加到父评论的children列表中。
                //如果父评论(CommentVo)的子评论列表为null,则新建一个列表。
                List<CommentVo> fatherCommentVoList = node.getContent().getChildrens();
                if(fatherCommentVoList==null){
                    fatherCommentVoList=new ArrayList<>();
                    fatherCommentVoList.add(childNode.getContent());
                    node.getContent().setChildrens(fatherCommentVoList);
                }else{
                    node.getContent().getChildrens().add(childNode.getContent());
                }
                //添加此节点的父亲节点
                childNode.setParentId(node.getId());
                return node;
            }
        }
        return null;

    }
}

