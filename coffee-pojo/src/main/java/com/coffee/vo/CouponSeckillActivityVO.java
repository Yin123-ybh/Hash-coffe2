package com.coffee.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券秒杀活动VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponSeckillActivityVO implements Serializable {
    private Long id;
    private String name;
    private String description;
    private Long couponId;
    private String couponName;             // 优惠券名称
    private Integer couponType;             // 优惠券类型
    private BigDecimal discountAmount;      // 优惠金额
    private BigDecimal discountRate;        // 折扣率
    private BigDecimal minAmount;           // 最低消费金额
    private Integer seckillStock;          // 秒杀库存
    private Integer perUserLimit;          // 每人限领数量
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private String statusText;
    private LocalDateTime createTime;
}

