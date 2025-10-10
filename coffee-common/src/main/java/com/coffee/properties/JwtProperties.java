package com.coffee.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "coffee.jwt")
public class JwtProperties {
    
    private String adminSecretKey;
    private String adminTokenName;
    private String userSecretKey;
    private String userTokenName;
    private Integer adminTtl;
    private Integer userTtl;
    
    // 添加getter方法别名以保持兼容性
    public Integer getUserTtl() {
        return userTtl;
    }
    
    public Integer getAdminTtl() {
        return adminTtl;
    }
}