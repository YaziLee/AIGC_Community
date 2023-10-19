package com.ices.aigccommunity.enity;

import lombok.Data;

@Data
public class User {

    private Long id;

    private String username;

    private String password;

    private Integer authority;

    private String avatar;

    private String tel;

    private String email; //改用为nickname
}
