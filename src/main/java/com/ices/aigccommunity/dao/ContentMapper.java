package com.ices.aigccommunity.dao;

import com.ices.aigccommunity.enity.Content;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContentMapper {
    long insertContent(Content content);

    List<Content> getAllContent();

    Content getById(long id);

    long updateBrowsed(long id);

    long incLiked(long id);

    long decLiked(long id);

    long incCollected(long id);

    long decCollected(long id);

}
