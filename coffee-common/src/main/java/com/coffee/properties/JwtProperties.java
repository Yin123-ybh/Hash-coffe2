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
}