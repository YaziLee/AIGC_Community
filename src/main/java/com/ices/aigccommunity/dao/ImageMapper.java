package com.ices.aigccommunity.dao;

import com.ices.aigccommunity.enity.Content;
import com.ices.aigccommunity.enity.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ImageMapper {
    long insertImage(Image image);

    long setFatherId(@Param("imageId")long imageId, @Param("fatherId")long fatherId);

    List<Image> getSon(long imageId);

    Image getById(long imageId);
}
