package com.ices.aigccommunity.controller.content;

import com.ices.aigccommunity.enity.Image;
import com.ices.aigccommunity.service.ImageService;
import com.ices.aigccommunity.utils.Result;
import com.ices.aigccommunity.utils.ResultGenerator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ImageController {

    @Resource
    ImageService imageService;

    @GetMapping("image/getSon")
    Result getSon(@RequestParam long imageId){

        List<Image> images=imageService.getSon(imageId);
        return ResultGenerator.genSuccessResult(images);
    }

    @GetMapping("image/getOne")
    Result getOne(@RequestParam long imageId){
        Image image=imageService.getOne(imageId);
        return ResultGenerator.genSuccessResult(image);
    }

    @GetMapping("image/getRealImageByContent")
    Result getRealImageByContent(@RequestParam long contentID){
        List<Image> realImages=imageService.getRealImageByContent(contentID);
        return ResultGenerator.genSuccessResult(realImages);
    }
}
