package com.coffee.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户优惠券实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 状态 1-未使用 2-已使用 3-已过期
     */
    private Integer status;

    /**
     * 领取时间
     */
    private LocalDateTime getTime;

    /**
     * 使用时间
     */
    private LocalDateTime useTime;

    /**
     * 使用的订单ID
     */
    private Long orderId;
}
