package com.ices.aigccommunity.dao;

import com.ices.aigccommunity.enity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int register(User user);

    User login(@Param("userName") String username, @Param("password") String password);

    User getById(long id);


}
