// AliOssUtil.java
package com.coffee.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.coffee.properties.AliOssProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class AliOssUtil {

    private static final Logger log = LoggerFactory.getLogger(AliOssUtil.class);

    @Autowired
    private AliOssProperties aliOssProperties;

    /**
     * 文件上传
     * @param file 文件
     * @return 文件访问路径
     */
    public String upload(MultipartFile file) {
        try {
            // 获取OSS客户端
            OSS ossClient = new OSSClientBuilder().build(
                    aliOssProperties.getEndpoint(),
                    aliOssProperties.getAccessKeyId(),
                    aliOssProperties.getAccessKeySecret()
            );

            // 获取文件输入流
            InputStream inputStream = file.getInputStream();

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + extension;

            // 按日期分类存储
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String objectName = "coffee/" + datePath + "/" + fileName;

            // 上传文件
            ossClient.putObject(aliOssProperties.getBucketName(), objectName, inputStream);

            // 关闭OSSClient
            ossClient.shutdown();

            // 返回文件访问路径
            String filePath = "https://" + aliOssProperties.getBucketName() + "." +
                    aliOssProperties.getEndpoint() + "/" + objectName;

            log.info("文件上传成功，路径：{}", filePath);
            return filePath;

        } catch (IOException e) {
            log.error("文件上传失败：{}", e.getMessage());
            throw new RuntimeException("文件上传失败");
        }
    }
}