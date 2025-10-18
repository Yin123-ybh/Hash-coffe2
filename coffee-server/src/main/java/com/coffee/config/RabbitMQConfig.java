package com.coffee.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 * 配置JSON消息转换器，替代默认的Java序列化器
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 配置JSON消息转换器
     * 使用Jackson将消息对象序列化为JSON格式
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

