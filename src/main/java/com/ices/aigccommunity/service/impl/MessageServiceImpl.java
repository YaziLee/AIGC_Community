package com.ices.aigccommunity.service.impl;

import com.ices.aigccommunity.common.ServiceResultEnum;
import com.ices.aigccommunity.controller.design.param.MessageParam;
import com.ices.aigccommunity.controller.design.vo.DesignMessageVO;
import com.ices.aigccommunity.controller.design.vo.MessageVO;
import com.ices.aigccommunity.controller.user.vo.UserVo;
import com.ices.aigccommunity.dao.MessageMapper;
import com.ices.aigccommunity.dao.UserMapper;
import com.ices.aigccommunity.enity.Message;
import com.ices.aigccommunity.enity.User;
import com.ices.aigccommunity.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    MessageMapper messageMapper;
    @Resource
    UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Transactional
    public String  addMessage(MessageParam messageParam,Long userID){
        Message message=new Message();
        logger.info("messageParam参数是{}",messageParam);
        BeanUtils.copyProperties(messageParam,message);
        message.setUserId(userID);

        messageMapper.insert(message);
        return ServiceResultEnum.SUCCESS.getResult();
    }

    public DesignMessageVO getProposeByDesign(Long id){
        //1 根据设计款id在massage表中查询所有消息
        List<Message> messageList=messageMapper.selectByDesignId(id);
        //更新为已读
        messageMapper.updateReadStatus(id);

        //设置一张Map表，key表示userID，value为List<MessageVO>
        Map<Long,List<MessageVO>> map=new HashMap<>();

        //2 依次处理每条消息
        for(Message message:messageList){
            Long userID=message.getUserId();
            MessageVO messageVO=new MessageVO();
            BeanUtils.copyProperties(message,messageVO);

            //如果userID不存在Map中，创建一个List并插入
            if(!map.containsKey(userID)){
                List<MessageVO> messageVOS=new LinkedList<>();
                messageVOS.add(messageVO);
                map.put(userID,messageVOS);
            }
            //否则，取出List添加一个MessageVO；
            else {
                List<MessageVO> messageVOS=map.get(userID);
                messageVOS.add(messageVO);
            }
        }

        //3 创建一个新的Map<UserVO,MessageVO>,遍历Map查询用户信息写入新的Map
        Map<String,List<MessageVO>> result=new HashMap<>();
        for(Long userID:map.keySet()){
            UserVo userVo=new UserVo();
            User user=userMapper.getById(userID);
            BeanUtils.copyProperties(user,userVo);
            String key="id="+userVo.getId().toString()+",email="+userVo.getEmail()+",avatar="+userVo.getAvatar();
            result.put(key,map.get(userID));
        }

        DesignMessageVO designMessageVO=new DesignMessageVO();
        designMessageVO.setResult(result);

        return designMessageVO;
    }

}
