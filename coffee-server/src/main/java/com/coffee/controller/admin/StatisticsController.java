package com.coffee.controller.admin;

import com.coffee.result.Result;
import com.coffee.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/statistics")
@Api(tags = "管理端统计相关接口")
@Slf4j
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    @ApiOperation("获取统计数据")
    public Result<Map<String, Object>> getStatisticsData(@RequestParam(defaultValue = "7days") String timeRange) {
        log.info("管理端获取统计数据：timeRange={}", timeRange);
        Map<String, Object> data = statisticsService.getStatisticsData(timeRange);
        return Result.success(data);
    }
    
    @GetMapping("/revenue")
    @ApiOperation("获取营业额趋势")
    public Result<Map<String, Object>> getRevenueTrend(@RequestParam(defaultValue = "7days") String timeRange) {
        log.info("管理端获取营业额趋势：timeRange={}", timeRange);
        Map<String, Object> data = statisticsService.getRevenueTrend(timeRange);
        return Result.success(data);
    }
    
    @GetMapping("/users")
    @ApiOperation("获取用户增长趋势")
    public Result<Map<String, Object>> getUserGrowth(@RequestParam(defaultValue = "7days") String timeRange) {
        log.info("管理端获取用户增长趋势：timeRange={}", timeRange);
        Map<String, Object> data = statisticsService.getUserGrowth(timeRange);
        return Result.success(data);
    }
    
    @GetMapping("/orders")
    @ApiOperation("获取订单状态分布")
    public Result<Map<String, Object>> getOrderStatusDistribution() {
        log.info("管理端获取订单状态分布");
        Map<String, Object> data = statisticsService.getOrderStatusDistribution();
        return Result.success(data);
    }
    
    @GetMapping("/products")
    @ApiOperation("获取商品销量排行")
    public Result<Map<String, Object>> getProductSalesRanking(@RequestParam(defaultValue = "10") Integer limit) {
        log.info("管理端获取商品销量排行：limit={}", limit);
        Map<String, Object> data = statisticsService.getProductSalesRanking(limit);
        return Result.success(data);
    }
}
