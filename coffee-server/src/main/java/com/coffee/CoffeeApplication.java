package com.coffee;

import com.coffee.properties.AliOssProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@EnableDiscoveryClient // 启用服务发现客户端
@Slf4j
@EnableConfigurationProperties(AliOssProperties.class)
public class CoffeeApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoffeeApplication.class, args);
        log.info("Coffee Server started and registered to Nacos");
    }
}
