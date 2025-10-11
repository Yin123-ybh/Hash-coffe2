package com.coffee.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO implements Serializable {
    private Long id;
    private String orderNo;
    private Long userId;
    private Long addressId;        // 收货地址ID
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal payAmount;
    private Integer status;
    private String statusText;      // 状态文本
    private Integer payMethod;
    private String payMethodText;   // 支付方式文本
    private LocalDateTime payTime;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<OrderItemVO> items; // 订单项列表
}