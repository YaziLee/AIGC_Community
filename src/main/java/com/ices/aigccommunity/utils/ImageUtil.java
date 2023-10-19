package com.ices.aigccommunity.utils;

import com.ices.aigccommunity.common.Constants;
import com.ices.aigccommunity.service.impl.ContentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class ImageUtil {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);
    public static List<File> imageCrop(File imageFile){
        logger.info("存取的临时文件是：{}",imageFile);
        List<File> cropedImages=new ArrayList();
        try {
            // 读取原始图片
            BufferedImage originalImage = ImageIO.read(imageFile);

            // 计算每个宫格的大小
            int squareSize = originalImage.getWidth() / 2;

            // 裁剪并保存4张单独的图片
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    // 创建一个新的BufferedImage来存储裁剪后的图片
                    BufferedImage croppedImage = new BufferedImage(squareSize, squareSize, BufferedImage.TYPE_INT_RGB);

                    // 裁剪图片
                    for (int x = 0; x < squareSize; x++) {
                        for (int y = 0; y < squareSize; y++) {
                            croppedImage.setRGB(x, y, originalImage.getRGB(i * squareSize + x, j * squareSize + y));
                        }
                    }

                    // 生成唯一的文件名
                    String fileName = UUID.randomUUID().toString() + ".jpg";

                    // 保存裁剪后的图片
                    File croppedFile = new File(Constants.FILE_SAVE_PATH + fileName);
                    ImageIO.write(croppedImage, "jpg", croppedFile);
                    cropedImages.add(croppedFile);

                }
            }

            System.out.println("图片裁剪完成！");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cropedImages;
    }
}
