package com.ices.aigccommunity.dao;

import com.ices.aigccommunity.enity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    int insert(Message message);

    List<Message> selectByDesignId(Long designId);

    int deleteByDesignId(Long designId);

    int updateReadStatus(Long designId);
}
