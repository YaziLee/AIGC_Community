package com.ices.aigccommunity.service;

import com.ices.aigccommunity.enity.Content;
import com.ices.aigccommunity.utils.Result;

import java.util.List;

public interface ContentService {
    String upload(String url1,String url2,String url3,String url4,String url5,String prompt,long publisherId);
    List<Content> getALL();
    Content getOne(long contentId);
    Result liked(long userId, long contentId);
    Result collected(long userId, long contentId);
}
