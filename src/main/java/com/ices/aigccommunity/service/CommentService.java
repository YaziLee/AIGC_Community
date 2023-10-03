package com.ices.aigccommunity.service;

import com.ices.aigccommunity.controller.content.param.CommentPublishParam;
import com.ices.aigccommunity.utils.Result;

public interface CommentService {

    Result getCommentsById(Long id);

    Result publish(CommentPublishParam commentPublishParam);
}
