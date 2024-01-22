package com.ices.aigccommunity.controller.design.vo;

import com.ices.aigccommunity.controller.user.vo.UserVo;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DesignMessageVO {

    private Map<String, List<MessageVO>> result;

}
