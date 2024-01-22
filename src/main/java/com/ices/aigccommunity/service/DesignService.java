package com.ices.aigccommunity.service;

import com.ices.aigccommunity.controller.design.param.DesignUploadParam;
import com.ices.aigccommunity.controller.design.vo.DesignDetailVO;

import java.util.List;

public interface DesignService {
    String upload(DesignUploadParam designUploadParam,Long userID);
    List getAll();
    DesignDetailVO getByID(Long id);
    List getByContent(Long contentID);
    String deleteByID(Long id);

    List getByDesigner(Long designerID);

}
