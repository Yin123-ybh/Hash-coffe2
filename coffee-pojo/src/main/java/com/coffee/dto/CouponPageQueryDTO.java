package com.coffee.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 优惠券分页查询DTO
 */
@Data
public class CouponPageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    //页码
    private int page = 1;

    //每页记录数
    private int pageSize = 10;

    //优惠券名称
    private String name;

    //优惠券类型 1-满减 2-折扣
    private Integer type;

    //状态 0-禁用 1-启用
    private Integer status;
}
