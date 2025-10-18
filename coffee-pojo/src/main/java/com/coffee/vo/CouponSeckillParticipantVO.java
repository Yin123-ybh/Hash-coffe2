package com.coffee.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 优惠券秒杀参与记录VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponSeckillParticipantVO implements Serializable {
    private Long id;                   // 记录ID
    private Long activityId;           // 活动ID
    private Long userId;               // 用户ID
    private String userName;           // 用户名称
    private Integer quantity;          // 参与数量
    private Integer status;            // 状态 0-待处理 1-成功 2-失败
    private String statusText;         // 状态文本
    private Long userCouponId;         // 关联用户优惠券ID
    private LocalDateTime createTime;  // 参与时间
}