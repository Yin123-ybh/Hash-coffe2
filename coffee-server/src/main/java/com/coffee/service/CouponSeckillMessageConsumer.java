// 文件路径：coffee-server/src/main/java/com/coffee/service/CouponSeckillMessageConsumer.java
package com.coffee.service;

import com.coffee.dto.CouponSeckillMessage;
import com.coffee.dto.CouponSeckillParticipateDTO;
import com.coffee.service.CouponSeckillActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CouponSeckillMessageConsumer {

    @Autowired
    private CouponSeckillActivityService couponSeckillActivityService;

    @RabbitListener(queues = "coupon.seckill.queue")
    public void handleCouponSeckillMessage(CouponSeckillMessage message) {
        log.info("处理优惠券秒杀消息：{}", message);

        try {
            CouponSeckillParticipateDTO participateDTO = CouponSeckillParticipateDTO.builder()
                    .activityId(message.getActivityId())
                    .quantity(message.getQuantity())
                    .build();

            couponSeckillActivityService.participateSeckill(message.getUserId(), participateDTO);
            log.info("优惠券秒杀消息处理成功：{}", message);
        } catch (Exception e) {
            log.error("优惠券秒杀消息处理失败：{}", message, e);
        }
    }
}