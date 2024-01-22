package com.ices.aigccommunity.dao;

import com.ices.aigccommunity.enity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> selectList(long contentId);

    int insertComment(Comment comment);

    int deleteByContentID(long id);
}
