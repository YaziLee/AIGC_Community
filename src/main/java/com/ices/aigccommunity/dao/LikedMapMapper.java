package com.ices.aigccommunity.dao;

import com.ices.aigccommunity.enity.LikedMap;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LikedMapMapper {
    int insertRecord(@Param("userId")long userId, @Param("contentId")long contentId);

    int deleteRecord(@Param("userId")long userId, @Param("contentId")long contentId);

    LikedMap selectByContentUserId(@Param("userId")long userId, @Param("contentId")long contentId);
}
