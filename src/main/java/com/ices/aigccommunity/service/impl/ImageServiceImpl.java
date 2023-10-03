package com.ices.aigccommunity.service.impl;

import com.ices.aigccommunity.dao.ImageMapper;
import com.ices.aigccommunity.enity.Image;
import com.ices.aigccommunity.service.ImageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Resource
    ImageMapper imageMapper;

    public List<Image> getSon(long imageId){
        return imageMapper.getSon(imageId);
    }

    public Image getOne(long imageId){
        return imageMapper.getById(imageId);
    }
}
