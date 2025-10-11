package com.coffee.service.impl;

import com.coffee.mapper.OrderMapper;
import com.coffee.mapper.ProductMapper;
import com.coffee.mapper.UserMapper;
import com.coffee.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private ProductMapper productMapper;

    @Override
    public Map<String, Object> getStatisticsData(String timeRange) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 基础统计数据
            result.put("totalOrders", orderMapper.countTotalOrders());
            result.put("totalUsers", userMapper.countTotalUsers());
            result.put("totalProducts", productMapper.countTotalProducts());
            result.put("totalRevenue", orderMapper.sumTotalRevenue());
            
            // 根据时间范围生成图表数据
            Map<String, Object> revenueData = generateRevenueData(timeRange);
            Map<String, Object> userData = generateUserData(timeRange);
            Map<String, Object> orderData = generateOrderData();
            Map<String, Object> salesData = generateSalesData();
            
            result.put("revenueData", revenueData);
            result.put("userData", userData);
            result.put("orderData", orderData);
            result.put("salesData", salesData);
            
        } catch (Exception e) {
            log.error("获取统计数据失败", e);
            // 返回默认数据
            result.put("totalOrders", 0);
            result.put("totalUsers", 0);
            result.put("totalProducts", 0);
            result.put("totalRevenue", 0.0);
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getRevenueTrend(String timeRange) {
        return generateRevenueData(timeRange);
    }

    @Override
    public Map<String, Object> getUserGrowth(String timeRange) {
        return generateUserData(timeRange);
    }

    @Override
    public Map<String, Object> getOrderStatusDistribution() {
        return generateOrderData();
    }

    @Override
    public Map<String, Object> getProductSalesRanking(Integer limit) {
        return generateSalesData();
    }

    private Map<String, Object> generateRevenueData(String timeRange) {
        Map<String, Object> data = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        
        // 根据时间范围生成数据
        int days = getDaysByTimeRange(timeRange);
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        
        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            dates.add(date.format(formatter));
            // 生成更真实的营业额数据（工作日较低，周末较高）
            double baseRevenue = 800 + Math.random() * 400;
            if (date.getDayOfWeek().getValue() >= 6) { // 周末
                baseRevenue *= 1.3;
            }
            values.add(Math.round(baseRevenue * 100.0) / 100.0);
        }
        
        data.put("dates", dates);
        data.put("values", values);
        return data;
    }
    
    private Map<String, Object> generateUserData(String timeRange) {
        Map<String, Object> data = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        
        // 根据时间范围生成数据
        int days = getDaysByTimeRange(timeRange);
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        
        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            dates.add(date.format(formatter));
            // 生成更真实的用户增长数据
            int baseUsers = 3 + (int)(Math.random() * 8);
            if (date.getDayOfWeek().getValue() >= 6) { // 周末
                baseUsers += 2;
            }
            values.add(baseUsers);
        }
        
        data.put("dates", dates);
        data.put("values", values);
        return data;
    }
    
    private Map<String, Object> generateOrderData() {
        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object>> statusList = new ArrayList<>();
        
        // 模拟订单状态分布数据
        Map<String, Object> status1 = new HashMap<>();
        status1.put("name", "已完成");
        status1.put("value", 45);
        statusList.add(status1);
        
        Map<String, Object> status2 = new HashMap<>();
        status2.put("name", "制作中");
        status2.put("value", 20);
        statusList.add(status2);
        
        Map<String, Object> status3 = new HashMap<>();
        status3.put("name", "已支付");
        status3.put("value", 15);
        statusList.add(status3);
        
        Map<String, Object> status4 = new HashMap<>();
        status4.put("name", "待支付");
        status4.put("value", 10);
        statusList.add(status4);
        
        Map<String, Object> status5 = new HashMap<>();
        status5.put("name", "已取消");
        status5.put("value", 5);
        statusList.add(status5);
        
        data.put("statusList", statusList);
        return data;
    }
    
    private Map<String, Object> generateSalesData() {
        Map<String, Object> data = new HashMap<>();
        List<String> names = Arrays.asList("美式咖啡", "拿铁咖啡", "卡布奇诺", "摩卡咖啡", "焦糖玛奇朵");
        List<Integer> values = Arrays.asList(45, 38, 32, 28, 25);
        
        data.put("names", names);
        data.put("values", values);
        return data;
    }
    
    /**
     * 根据时间范围获取天数
     * @param timeRange 时间范围
     * @return 天数
     */
    private int getDaysByTimeRange(String timeRange) {
        switch (timeRange.toLowerCase()) {
            case "1day":
            case "1days":
                return 1;
            case "7day":
            case "7days":
                return 7;
            case "30day":
            case "30days":
                return 30;
            case "90day":
            case "90days":
                return 90;
            case "1year":
            case "1years":
                return 365;
            default:
                return 7; // 默认7天
        }
    }
}
