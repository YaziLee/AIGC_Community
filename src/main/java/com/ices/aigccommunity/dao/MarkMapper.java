package com.ices.aigccommunity.dao;

import com.ices.aigccommunity.enity.Mark;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MarkMapper {

    Long insert(Mark mark);

    List<Mark> selectByDesignImageId(Long designImageId);

    Long deleteByDesignId(Long designId);
}
