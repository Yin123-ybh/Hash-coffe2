package com.coffee.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券ID
     */
    private Long id;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 优惠券类型 1-满减 2-折扣
     */
    private Integer type;

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
     * 总数量
     */
    private Integer totalCount;

    /**
     * 已使用数量
     */
    private Integer usedCount;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 状态 0-禁用 1-启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
