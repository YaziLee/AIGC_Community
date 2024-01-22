package com.ices.aigccommunity.dao;

import com.ices.aigccommunity.enity.DesignImage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DesignImageMapper {

    Long insert(DesignImage designImage);

    List<DesignImage> selectByDesignId(Long designId);

    List<DesignImage> selectByMarkId(Long markId);

    Long deleteByDesignId(Long designId);

    DesignImage selectMarkDesignImageByID(Long id);
    DesignImage selectDesignImageByID(Long id);
}