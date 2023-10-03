package com.ices.aigccommunity.service.impl;

import com.ices.aigccommunity.common.QiniuyunCons;
import com.ices.aigccommunity.service.UploadTokenService;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.stereotype.Service;

@Service
public class UploadTokenServiceImpl implements UploadTokenService {
    public String getUploadToken(boolean forever){
        String ACCESS_KEY = QiniuyunCons.ACCESS_KEY;
        String SECRET_KEY = QiniuyunCons.SECRET_KEY;
        String BUCKET = QiniuyunCons.BUCKET;

        Auth auth = Auth.create(ACCESS_KEY,SECRET_KEY);
        long expireSeconds = 7200; //上传凭证的有效时间
        int days = 1; //文建过期天数
        StringMap putPolicy = new StringMap();
        if(!forever)
            putPolicy.put("deleteAfterDays",days);
        String token= auth.uploadToken(BUCKET,null,expireSeconds,putPolicy);
        return token;
    }
}
