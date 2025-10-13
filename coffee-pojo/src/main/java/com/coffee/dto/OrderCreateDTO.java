package com.coffee.dto;

import lombok.Data;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单创建DTO
 */
@Data
public class OrderCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "订单商品不能为空")
    @Valid
    private List<OrderItemDTO> items;

    @NotNull(message = "收货地址ID不能为空")
    private Long addressId;

    private Long couponId;
    
    private Long userCouponId;

    private String remark;

    @NotNull(message = "订单总金额不能为空")
    private BigDecimal totalAmount;

    /**
     * 订单商品项DTO
     */
    @Data
    public static class OrderItemDTO implements Serializable {
        
        @NotNull(message = "商品ID不能为空")
        private Long productId;

        @NotNull(message = "商品数量不能为空")
        private Integer quantity;
    }
}