// 文件路径：coffee-pojo/src/main/java/com/coffee/dto/StatisticsDTO.java
package com.coffee.dto;

import lombok.Data;
import lombok.Builder;


@Data
@Builder
public class StatisticsDTO {
    // 营业额数据
    private RevenueDataDTO revenueData;

    // 用户数据
    private UserDataDTO userData;

    // 订单数据
    private OrderDataDTO orderData;

    // 销量数据
    private SalesDataDTO salesData;
}