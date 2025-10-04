package com.coffee.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "coffee.jwt")
public class JwtProperties {
    /**
     * 管理员JWT签名密钥
     */
    private String adminSecretKey;
    
    /**
     * 管理员JWT过期时间(毫秒)
     */
    private Long adminTtl;
    
    /**
     * 管理员令牌名称
     */
    private String adminTokenName;
    
    /**
     * 用户令牌名称
     */
    private String userTokenName;
    
    /**
     * 用户JWT签名密钥
     */
    private String userSecretKey;
    
    /**
     * 用户JWT过期时间(毫秒)
     */
    private Long userTtl;
}