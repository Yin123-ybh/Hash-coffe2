package com.coffee.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 订单分页查询DTO
 */
@Data
public class OrderPageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int page = 1;
    private int pageSize = 10;
    private String orderNo;
    private Integer status;
    private String beginTime;
    private String endTime;
}