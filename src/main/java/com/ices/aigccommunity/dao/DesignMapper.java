package com.ices.aigccommunity.dao;

import com.ices.aigccommunity.enity.Design;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DesignMapper {

    Long insert(Design design);

    Design selectById(Long id);

    List<Design> selectByDesignFromContentId(Long designFromContentId);

    Long deleteById(Long id);

    List<Design> selectAll();

    List<Design> getByDesigner(Long designerID);
}
