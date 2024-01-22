package com.ices.aigccommunity.controller.design;

import com.ices.aigccommunity.common.ServiceResultEnum;
import com.ices.aigccommunity.config.annotation.TokenToUser;
import com.ices.aigccommunity.controller.design.param.MessageParam;
import com.ices.aigccommunity.controller.design.vo.DesignMessageVO;
import com.ices.aigccommunity.enity.User;
import com.ices.aigccommunity.service.MessageService;
import com.ices.aigccommunity.utils.Result;
import com.ices.aigccommunity.utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Resource
    MessageService messageService;

    @PostMapping("/design/propose")
    public Result propose(@RequestBody MessageParam messageParam,@TokenToUser User loginUser) {
        logger.info("用户loginUser:{},请求对设计款提交建议", loginUser.toString());
        String result= messageService.addMessage(messageParam,loginUser.getId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    @GetMapping("/design/getProposeByDesign")
    public Result getProposeByDesign(Long id) {
        DesignMessageVO designMessageVO=messageService.getProposeByDesign(id);
        return ResultGenerator.genSuccessResult(designMessageVO);
    }


}
