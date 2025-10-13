package com.coffee.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户优惠券视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户优惠券ID
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
     * 优惠券名称
     */
    private String couponName;

    /**
     * 优惠券类型 1-满减 2-折扣
     */
    private Integer couponType;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 折扣率
     */
    private BigDecimal discountRate;

    /**
     * 最低消费金额
     */
    private BigDecimal minAmount;

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
     * 优惠券开始时间
     */
    private LocalDateTime startTime;

    /**
     * 优惠券结束时间
     */
    private LocalDateTime endTime;

    /**
     * 使用的订单ID
     */
    private Long orderId;
}