package com.coffee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 优惠券秒杀活动DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponSeckillActivityDTO implements Serializable {
    private Long id;                   // 活动ID（更新时使用）
    private String name;
    private String description;
    private Long couponId;
    private Integer seckillStock;
    private Integer perUserLimit;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

