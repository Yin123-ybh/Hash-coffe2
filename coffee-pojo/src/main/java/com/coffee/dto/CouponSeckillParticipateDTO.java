package com.coffee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 优惠券秒杀参与DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponSeckillParticipateDTO implements Serializable {
    private Long activityId;
    private Integer quantity;
}

