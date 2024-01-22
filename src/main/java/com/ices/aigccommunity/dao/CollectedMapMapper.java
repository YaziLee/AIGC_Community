package com.ices.aigccommunity.dao;

import com.ices.aigccommunity.enity.CollectedMap;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CollectedMapMapper {
    int insertRecord(@Param("userId")long userId, @Param("contentId")long contentId);

    int deleteRecord(@Param("userId")long userId, @Param("contentId")long contentId);

    CollectedMap selectByContentUserId(@Param("userId")long userId, @Param("contentId")long contentId);

    int deleteByContentID(long id);
}
