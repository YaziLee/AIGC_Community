package com.ices.aigccommunity.service;

import com.ices.aigccommunity.enity.User;

public interface UserService {

    String register(String loginName, String password,String avatar,String nickname);

    String login(String userName, String password);

    Boolean logout(Long userId);

    User getById(long id);

    User getByLoginname(String loginName);


}
