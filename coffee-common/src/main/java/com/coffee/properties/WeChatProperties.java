package com.coffee.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "coffee.wechat")
public class WeChatProperties {
    
    private String appid;
    private String secret;
}