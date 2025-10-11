package com.coffee.service;

import java.util.Map;

public interface StatisticsService {
    
    /**
     * 获取统计数据
     * @param timeRange 时间范围
     * @return 统计数据
     */
    Map<String, Object> getStatisticsData(String timeRange);
    
    /**
     * 获取营业额趋势
     * @param timeRange 时间范围
     * @return 营业额趋势数据
     */
    Map<String, Object> getRevenueTrend(String timeRange);
    
    /**
     * 获取用户增长趋势
     * @param timeRange 时间范围
     * @return 用户增长数据
     */
    Map<String, Object> getUserGrowth(String timeRange);
    
    /**
     * 获取订单状态分布
     * @return 订单状态分布数据
     */
    Map<String, Object> getOrderStatusDistribution();
    
    /**
     * 获取商品销量排行
     * @param limit 限制数量
     * @return 商品销量排行数据
     */
    Map<String, Object> getProductSalesRanking(Integer limit);
}
