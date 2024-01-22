package com.ices.aigccommunity.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ices.aigccommunity.common.Constants;
import com.ices.aigccommunity.common.QiniuyunCons;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.util.Auth;


public class QiniuyunUtil {


    public static File downloadFile(String imageUrl) {
        String savePath = Constants.FILE_SAVE_PATH;
        try {
            // 创建URL对象
            URL url = new URL(imageUrl);

            // 打开URL连接
            URLConnection conn = url.openConnection();

            // 获取文件名
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

            // 创建输入流和输出流
            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(savePath + "/" + fileName));

            // 缓冲区大小
            byte[] buffer = new byte[1024];
            int len;

            // 从输入流读取数据，并写入输出流
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

            // 关闭流
            out.flush();
            out.close();
            in.close();

            System.out.println("图片下载完成！");
            return new File(savePath + "/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String uploadFile(File imageFile) {

        // 七牛云配置信息
        String accessKey = QiniuyunCons.ACCESS_KEY;
        String secretKey = QiniuyunCons.SECRET_KEY;
        String bucket = QiniuyunCons.BUCKET;
        String domain = QiniuyunCons.DOMAIN_BUCKET;


        try {
            // 创建七牛云配置对象
            Configuration configuration = new Configuration();
            // 创建上传管理器
            UploadManager uploadManager = new UploadManager(configuration);
            // 生成上传凭证
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            // 获取上传的文件流
            InputStream inputStream = new FileInputStream(imageFile);
            // 生成文件名
            String fileName = UUID.randomUUID().toString();

            // 上传文件
            uploadManager.put(inputStream, fileName, upToken, null, null);

            // 构建图片的URL
            String imageUrl = domain + "/" + fileName;

            return imageUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 从七牛云删除图片
     * @param fileNameList 图片名集合
     * @return 返回每张图片的删除结果
     */
    public static List<String> deleteImage(String[] fileNameList){
        //用Json格式传数组时直接传["xxx", "xxx", "xxx", "xxx"]
        // 七牛云配置信息
        String accessKey = QiniuyunCons.ACCESS_KEY;
        String secretKey = QiniuyunCons.SECRET_KEY;
        String bucket = QiniuyunCons.BUCKET;
        String domain = QiniuyunCons.DOMAIN_BUCKET;
        Auth auth = Auth.create(accessKey, secretKey);
        Configuration cfg = new Configuration();
        BucketManager bucketManager = new BucketManager(auth, cfg);
        //创建一个用来接收删除结果的集合
        List<String> list = new ArrayList<>();
        try {
            //单次批量请求的文件数量不得超过1000
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucket, fileNameList);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < fileNameList.length; i++) {
                BatchStatus status = batchStatusList[i];
                if (status.code == 200) {//成功返回文件名+true并添加进集合
                    list.add("{fileName:"+fileNameList[i]+",deleteResult:true}");
                }else {//失败返回文件名+false并添加进集合
                    list.add("{fileName:"+fileNameList[i]+",deleteResult:false}");
                }
            }
        } catch (QiniuException ex) {
            System.err.println("七牛云ERROR:" + ex.response.toString());
        }
        return list;
    }
}
