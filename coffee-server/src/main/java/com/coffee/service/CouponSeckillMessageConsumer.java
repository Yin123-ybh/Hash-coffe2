// 文件路径：coffee-server/src/main/java/com/coffee/service/CouponSeckillMessageConsumer.java
package com.coffee.service;

import com.coffee.dto.CouponSeckillMessage;
import com.coffee.dto.CouponSeckillParticipateDTO;
import com.coffee.service.impl.CouponSeckillActivityServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CouponSeckillMessageConsumer {

    @Autowired
    private CouponSeckillActivityServiceImpl couponSeckillActivityService;

    /**
     * 监听RabbitMQ队列，消费秒杀消息
     * 这个方法会被RabbitMQ自动调用，处理队列中的消息
     */
    @RabbitListener(queues = "coupon.seckill.queue")
    public void handleCouponSeckillMessage(CouponSeckillMessage message) {
        log.info("========== 开始处理秒杀消息 ==========");
        log.info("消息内容：{}", message);

        try {
            // 1. 构建参与DTO
            CouponSeckillParticipateDTO participateDTO = CouponSeckillParticipateDTO.builder()
                    .activityId(message.getActivityId())
                    .quantity(message.getQuantity())
                    .build();

            // 2. 调用Service处理秒杀（执行Redis Lua脚本和数据库操作）
            couponSeckillActivityService.processSeckillMessage(message.getUserId(), participateDTO);
            
            log.info("========== 秒杀消息处理成功 ==========");
            log.info("用户ID：{}，活动ID：{}，数量：{}", 
                    message.getUserId(), message.getActivityId(), message.getQuantity());
        } catch (Exception e) {
            log.error("========== 秒杀消息处理失败 ==========");
            log.error("失败消息：{}", message);
            log.error("错误信息：{}", e.getMessage(), e);
            
            // 注意：这里异常会被RabbitMQ捕获，根据配置决定是否重试
            // 如果重试3次后仍然失败，消息可能会进入死信队列
            throw new RuntimeException("秒杀处理失败", e);
        }
    }
}