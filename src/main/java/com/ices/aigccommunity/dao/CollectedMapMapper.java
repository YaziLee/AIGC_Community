package com.ices.aigccommunity.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CollectedMapMapper {
    int insertRecord(@Param("userId")long userId, @Param("contentId")long contentId);
}
