package com.ices.aigccommunity.service;

import com.ices.aigccommunity.controller.design.param.MessageParam;
import com.ices.aigccommunity.controller.design.vo.DesignMessageVO;
import org.springframework.web.bind.annotation.PathVariable;

public interface MessageService {
    String addMessage(MessageParam messageParam,Long userID);

    DesignMessageVO getProposeByDesign(Long id);

}
