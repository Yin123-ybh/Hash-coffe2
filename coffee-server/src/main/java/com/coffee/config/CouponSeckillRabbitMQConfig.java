// 文件路径：coffee-server/src/main/java/com/coffee/config/CouponSeckillRabbitMQConfig.java
package com.coffee.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouponSeckillRabbitMQConfig {

    // 优惠券秒杀队列
    public static final String COUPON_SECKILL_QUEUE = "coupon.seckill.queue";
    public static final String COUPON_SECKILL_EXCHANGE = "coupon.seckill.exchange";
    public static final String COUPON_SECKILL_ROUTING_KEY = "coupon.seckill.participate";

    @Bean
    public Queue couponSeckillQueue() {
        return QueueBuilder.durable(COUPON_SECKILL_QUEUE).build();
    }

    @Bean
    public DirectExchange couponSeckillExchange() {
        return new DirectExchange(COUPON_SECKILL_EXCHANGE);
    }

    @Bean
    public Binding couponSeckillBinding() {
        return BindingBuilder.bind(couponSeckillQueue())
                .to(couponSeckillExchange())
                .with(COUPON_SECKILL_ROUTING_KEY);
    }
}