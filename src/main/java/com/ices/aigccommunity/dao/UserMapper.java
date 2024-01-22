package com.ices.aigccommunity.dao;

import com.ices.aigccommunity.enity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    int register(User user);

    User login(@Param("userName") String username, @Param("password") String password);

    User getById(long id);

    User getByLoginname(String loginName);

    List<User> selectAllDesigners();

    int updateInfo(User user);


}
