package com.ices.aigccommunity.service.impl;

import com.ices.aigccommunity.controller.content.CommentController;
import com.ices.aigccommunity.controller.content.param.CommentPublishParam;
import com.ices.aigccommunity.controller.content.vo.CommentVo;
import com.ices.aigccommunity.controller.user.vo.UserVo;
import com.ices.aigccommunity.dao.CommentMapper;
import com.ices.aigccommunity.enity.Comment;
import com.ices.aigccommunity.enity.User;
import com.ices.aigccommunity.service.CommentService;
import com.ices.aigccommunity.service.UserService;
import com.ices.aigccommunity.utils.MyTreeNode;
import com.ices.aigccommunity.utils.Result;
import com.ices.aigccommunity.utils.ResultGenerator;
import com.ices.aigccommunity.utils.TreeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    CommentMapper commentMapper;

    @Resource
    UserService userService;

    @Resource
    TreeUtils treeUtils;

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    public Result getCommentsById(Long contentId) {

        //查出内容的所有评论
        List<Comment> comments = commentMapper.selectList(contentId);
        //所有CommentVo对象的列表
        List<CommentVo> commentVos=new ArrayList<>();
        //补充评论内容 copy属性转换为vo

        logger.info("属于内容的评论有：{}",comments);
        for (Comment comment : comments) {
            //从entity中提取出vo信息
            CommentVo commentVo=new CommentVo();
            logger.info("评论的内容是：{}",comment);
            BeanUtils.copyProperties(comment,commentVo);

            //作者信息
            User author = userService.getById(comment.getUserId());
            UserVo authorVo=new UserVo();
            BeanUtils.copyProperties(author,authorVo);
            commentVo.setAuthor(authorVo);

            //toUser信息 level>1 说明有回复对象
            if(comment.getLevel()>1){
                UserVo toUserVo=new UserVo();
                User toUser=userService.getById(comment.getToUid());
                BeanUtils.copyProperties(toUser,toUserVo);
                commentVo.setToUser(toUserVo);
            }
            commentVos.add(commentVo);
        }

        //构造成树
        MyTreeNode myTree = treeUtils.buildTree(commentVos);
        //根节点的子节点（即level为1的评论 ）
        List<MyTreeNode> children = myTree.getChildren();
        List<CommentVo> level1CommentVo=new ArrayList<>();
        for (MyTreeNode child : children) {
            level1CommentVo.add(child.getContent());
        }
        return ResultGenerator.genSuccessResult(level1CommentVo);
    }

    public Result publish(CommentPublishParam commentPublishParam){

        Comment comment=new Comment();
        Date date=new Date();
        comment.setReplyTime(date);
        comment.setUserId(commentPublishParam.getUserID());
        comment.setBody(commentPublishParam.getBody());
        comment.setContentId(commentPublishParam.getContentID());
        comment.setToUid(commentPublishParam.getToUid());
        comment.setLevel(commentPublishParam.getLevel());
        comment.setParentId(commentPublishParam.getParentId());
        commentMapper.insertComment(comment);

        return ResultGenerator.genSuccessResult();
    }
}
