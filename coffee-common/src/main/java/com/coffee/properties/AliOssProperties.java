package com.coffee.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "coffee.alioss")
public class AliOssProperties {
    
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}