package com.coffee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 优惠券秒杀消息DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponSeckillMessage implements Serializable {
    private Long userId;
    private Long activityId;
    private Integer quantity;
    private Long timestamp;
}

