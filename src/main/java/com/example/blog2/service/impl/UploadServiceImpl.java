package com.example.blog2.service.impl;

import com.example.blog2.service.UploadService;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

// 该类暂时没有用到，是上传文件到云服务的接口
@Service
public class UploadServiceImpl implements UploadService {
    private static final Log log = LogFactory.getLog(UploadServiceImpl.class);
    String accessKey = "BJaCYEKfuHLK5yu8xw-0UOgBSrEJThopbC2n0Ln2";
    String secretKey = "PsAb0hICTJNyF_0xyDfl-D8tg2VhtVseGmlhrZ9C";
    String bucket = "hanphone-picture";
    String defaultUrl = "https://hanphone.top/images/default.png";
    /**
     * 将图片上传到七牛云
     */
    public String upload(MultipartFile file,String path) {
        if (file.isEmpty()) {
            return defaultUrl;
        }
        Configuration cfg = new Configuration(Region.regionAs0());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        cfg.useHttpsDomains = true;
        UploadManager uploadManager = new UploadManager(cfg);
        // String suffixName = fileName.substring(fileName.lastIndexOf("."));//获取后缀
        //fileName = UUID.randomUUID() + suffixName;//生成唯一文件名
        String fileName = file.getOriginalFilename();//上传的文件名
        fileName = String.format("%s%s",path,fileName);
        log.info("filename:"+fileName);
        byte[] fileBytes;//换为byte数组
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        Response response;
        try {
            response = uploadManager.put(fileBytes,fileName, upToken);
            log.info("上传七牛云成功");
        } catch (QiniuException e) {
            throw new RuntimeException(e);
        }
        // 解析上传成功的结果
        try {
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException e) {
            throw new RuntimeException(e);
        }
        String domainOfBucket = "https://hanphone.top";
        String finalUrl = String.format("%s/%s", domainOfBucket, fileName);
        log.info(finalUrl);
        return finalUrl;
    }
}

