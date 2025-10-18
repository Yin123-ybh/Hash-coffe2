package com.coffee.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class CouponSeckillRedisUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private DefaultRedisScript<List> couponSeckillScript;

    @PostConstruct
    public void init(){
        //加载优惠券秒杀lua脚本
        couponSeckillScript = new DefaultRedisScript<>();
        couponSeckillScript.setScriptSource(new ResourceScriptSource(
                new ClassPathResource("scripts/coupon_seckill_participate.lua")));
        couponSeckillScript.setResultType(List.class);
    }
    /**
     * 执行优惠券秒杀Lua脚本
     */
    public List<Object> executeCouponSeckillScript(String stockKey, String participantsKey,
                                                   String userId, String quantity, String perUserLimit) {
        return redisTemplate.execute(couponSeckillScript,
                Arrays.asList(stockKey, participantsKey),
                userId, quantity, perUserLimit);
    }

    /**
     * 预热优惠券秒杀库存
     */
    public void warmUpCouponSeckillStock(Long activityId, Integer stock) {
        String stockKey = "coupon_seckill:stock:" + activityId;
        redisTemplate.opsForValue().set(stockKey, stock);
        log.info("预热优惠券秒杀库存成功，活动ID：{}，库存：{}", activityId, stock);
    }

    /**
     * 获取优惠券秒杀库存
     */
    public Integer getCouponSeckillStock(String key) {
        Object stock = redisTemplate.opsForValue().get(key);
        return stock != null ? Integer.parseInt(stock.toString()) : 0;
    }
}
