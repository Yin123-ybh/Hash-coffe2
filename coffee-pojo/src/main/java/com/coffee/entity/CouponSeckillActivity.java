package com.coffee.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 优惠券秒杀活动实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponSeckillActivity implements Serializable {
    private Long id;
    private String name;                    // 活动名称
    private String description;             // 活动描述
    private Long couponId;                  // 优惠券ID
    private Integer seckillStock;           // 秒杀库存数量
    private Integer perUserLimit;           // 每人限领数量
    private LocalDateTime startTime;        // 开始时间
    private LocalDateTime endTime;          // 结束时间
    private Integer status;                 // 状态 0-未开始 1-进行中 2-已结束 3-已取消
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

