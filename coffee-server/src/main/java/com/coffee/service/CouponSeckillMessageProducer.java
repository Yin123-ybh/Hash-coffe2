// 文件路径：coffee-server/src/main/java/com/coffee/service/CouponSeckillMessageProducer.java
package com.coffee.service;

import com.coffee.config.CouponSeckillRabbitMQConfig;
import com.coffee.dto.CouponSeckillMessage;
import com.coffee.dto.CouponSeckillParticipateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CouponSeckillMessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendCouponSeckillMessage(Long userId, CouponSeckillParticipateDTO participateDTO) {
        CouponSeckillMessage message = CouponSeckillMessage.builder()
                .userId(userId)
                .activityId(participateDTO.getActivityId())
                .quantity(participateDTO.getQuantity())
                .timestamp(System.currentTimeMillis())
                .build();

        rabbitTemplate.convertAndSend(
                CouponSeckillRabbitMQConfig.COUPON_SECKILL_EXCHANGE,
                CouponSeckillRabbitMQConfig.COUPON_SECKILL_ROUTING_KEY,
                message
        );

        log.info("发送优惠券秒杀消息：{}", message);
    }
}