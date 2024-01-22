package com.ices.aigccommunity.controller.user.param;

import lombok.Data;

@Data
public class UserInfoChangeParam {
    String passward;
    String avatar;
    //昵称
    String email;
    //个性签名
    String tel;
}
