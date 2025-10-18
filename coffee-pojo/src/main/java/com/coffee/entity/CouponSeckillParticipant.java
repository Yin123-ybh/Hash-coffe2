package com.coffee.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 优惠券秒杀参与记录实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponSeckillParticipant implements Serializable {
    private Long id;
    private Long activityId;                // 活动ID
    private Long userId;                    // 用户ID
    private Integer quantity;               // 参与数量
    private Integer status;                 // 状态 0-待处理 1-成功 2-失败
    private Long userCouponId;              // 关联用户优惠券ID
    private LocalDateTime createTime;
}

