package com.ices.aigccommunity.service;

import com.ices.aigccommunity.enity.Image;

import java.util.List;

public interface ImageService {
    List<Image> getSon(long imageId);
    Image getOne(long imageId);

    List<Image> getRealImageByContent(long contentID);
}
