package com.ices.aigccommunity.service;

import com.ices.aigccommunity.controller.content.param.ContentUploadParam;
import com.ices.aigccommunity.controller.content.vo.ContentDetailVO;
import com.ices.aigccommunity.enity.Content;
import com.ices.aigccommunity.utils.Result;

import java.util.List;

public interface ContentService {
    String upload(ContentUploadParam contentUploadParam);
    List<Content> getALL();
    ContentDetailVO getDetail(long userId, long contentId);
    Result liked(long userId, long contentId);
    Result collected(long userId, long contentId);

    Result disLiked(long userId, long contentId);

    Result disCollected(long userId, long contentId);

    String deleteByID(int id);
}
