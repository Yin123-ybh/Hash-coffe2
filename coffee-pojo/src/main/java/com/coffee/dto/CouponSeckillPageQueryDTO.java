package com.coffee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 优惠券秒杀活动分页查询DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponSeckillPageQueryDTO implements Serializable {
    private Integer page = 1;           // 页码
    private Integer pageSize = 10;      // 页大小
    private String name;               // 活动名称
    private Integer status;             // 活动状态
    private Long couponId;             // 优惠券ID
}