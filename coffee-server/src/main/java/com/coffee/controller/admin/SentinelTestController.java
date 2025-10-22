package com.coffee.controller.admin;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.coffee.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Sentinel 测试控制器
 * 用于测试流量控制和熔断降级功能
 */
@RestController
@RequestMapping("/admin/sentinel")
@Api(tags = "Sentinel测试接口")
@Slf4j
public class SentinelTestController {

    /**
     * 测试流控规则 - 商品查询
     */
    @GetMapping("/test/product-query")
    @ApiOperation("测试商品查询流控")
    @SentinelResource(value = "productQuery", blockHandler = "handleProductQueryBlock")
    public Result<Map<String, Object>> testProductQuery() {
        log.info("测试商品查询接口");
        Map<String, Object> result = new HashMap<>();
        result.put("message", "商品查询成功");
        result.put("timestamp", System.currentTimeMillis());
        result.put("data", "模拟商品数据");
        return Result.success(result);
    }

    /**
     * 测试流控规则 - 订单查询
     */
    @GetMapping("/test/order-query")
    @ApiOperation("测试订单查询流控")
    @SentinelResource(value = "orderQuery", blockHandler = "handleOrderQueryBlock")
    public Result<Map<String, Object>> testOrderQuery() {
        log.info("测试订单查询接口");
        Map<String, Object> result = new HashMap<>();
        result.put("message", "订单查询成功");
        result.put("timestamp", System.currentTimeMillis());
        result.put("data", "模拟订单数据");
        return Result.success(result);
    }

    /**
     * 测试流控规则 - 用户查询
     */
    @GetMapping("/test/user-query")
    @ApiOperation("测试用户查询流控")
    @SentinelResource(value = "userQuery", blockHandler = "handleUserQueryBlock")
    public Result<Map<String, Object>> testUserQuery() {
        log.info("测试用户查询接口");
        Map<String, Object> result = new HashMap<>();
        result.put("message", "用户查询成功");
        result.put("timestamp", System.currentTimeMillis());
        result.put("data", "模拟用户数据");
        return Result.success(result);
    }

    /**
     * 测试熔断规则 - 用户服务
     */
    @GetMapping("/test/user-service")
    @ApiOperation("测试用户服务熔断")
    @SentinelResource(value = "userService", blockHandler = "handleUserServiceBlock", fallback = "handleUserServiceFallback")
    public Result<Map<String, Object>> testUserService() {
        log.info("测试用户服务接口");
        
        // 模拟服务调用延迟
        try {
            Thread.sleep(150); // 模拟150ms延迟，可能触发熔断
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("服务调用异常", e);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", "用户服务调用成功");
        result.put("timestamp", System.currentTimeMillis());
        result.put("data", "模拟用户服务数据");
        return Result.success(result);
    }

    /**
     * 测试熔断规则 - 商品服务
     */
    @GetMapping("/test/product-service")
    @ApiOperation("测试商品服务熔断")
    @SentinelResource(value = "productService", blockHandler = "handleProductServiceBlock", fallback = "handleProductServiceFallback")
    public Result<Map<String, Object>> testProductService() {
        log.info("测试商品服务接口");
        
        // 模拟异常情况
        if (Math.random() > 0.7) {
            throw new RuntimeException("模拟服务异常");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", "商品服务调用成功");
        result.put("timestamp", System.currentTimeMillis());
        result.put("data", "模拟商品服务数据");
        return Result.success(result);
    }

    /**
     * 测试熔断规则 - 订单服务
     */
    @GetMapping("/test/order-service")
    @ApiOperation("测试订单服务熔断")
    @SentinelResource(value = "orderService", blockHandler = "handleOrderServiceBlock", fallback = "handleOrderServiceFallback")
    public Result<Map<String, Object>> testOrderService() {
        log.info("测试订单服务接口");
        
        // 模拟异常情况
        if (Math.random() > 0.8) {
            throw new RuntimeException("模拟订单服务异常");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", "订单服务调用成功");
        result.put("timestamp", System.currentTimeMillis());
        result.put("data", "模拟订单服务数据");
        return Result.success(result);
    }

    // ========== 流控降级处理方法 ==========

    public Result<Map<String, Object>> handleProductQueryBlock(BlockException ex) {
        log.warn("商品查询触发流控，请求被限流: {}", ex.getMessage());
        Map<String, Object> result = new HashMap<>();
        result.put("message", "系统繁忙，请稍后重试");
        result.put("timestamp", System.currentTimeMillis());
        result.put("blocked", true);
        return Result.error("商品查询服务繁忙，请稍后重试");
    }

    public Result<Map<String, Object>> handleOrderQueryBlock(BlockException ex) {
        log.warn("订单查询触发流控，请求被限流: {}", ex.getMessage());
        Map<String, Object> result = new HashMap<>();
        result.put("message", "系统繁忙，请稍后重试");
        result.put("timestamp", System.currentTimeMillis());
        result.put("blocked", true);
        return Result.error("订单查询服务繁忙，请稍后重试");
    }

    public Result<Map<String, Object>> handleUserQueryBlock(BlockException ex) {
        log.warn("用户查询触发流控，请求被限流: {}", ex.getMessage());
        Map<String, Object> result = new HashMap<>();
        result.put("message", "系统繁忙，请稍后重试");
        result.put("timestamp", System.currentTimeMillis());
        result.put("blocked", true);
        return Result.error("用户查询服务繁忙，请稍后重试");
    }

    // ========== 熔断降级处理方法 ==========

    public Result<Map<String, Object>> handleUserServiceBlock(BlockException ex) {
        log.warn("用户服务触发流控，请求被限流: {}", ex.getMessage());
        Map<String, Object> result = new HashMap<>();
        result.put("message", "用户服务繁忙，请稍后重试");
        result.put("timestamp", System.currentTimeMillis());
        result.put("blocked", true);
        return Result.error("用户服务繁忙，请稍后重试");
    }

    public Result<Map<String, Object>> handleUserServiceFallback(Throwable ex) {
        log.error("用户服务熔断降级，服务不可用: {}", ex.getMessage());
        Map<String, Object> result = new HashMap<>();
        result.put("message", "用户服务暂时不可用，请稍后重试");
        result.put("timestamp", System.currentTimeMillis());
        result.put("fallback", true);
        return Result.error("用户服务暂时不可用，请稍后重试");
    }

    public Result<Map<String, Object>> handleProductServiceBlock(BlockException ex) {
        log.warn("商品服务触发流控，请求被限流: {}", ex.getMessage());
        Map<String, Object> result = new HashMap<>();
        result.put("message", "商品服务繁忙，请稍后重试");
        result.put("timestamp", System.currentTimeMillis());
        result.put("blocked", true);
        return Result.error("商品服务繁忙，请稍后重试");
    }

    public Result<Map<String, Object>> handleProductServiceFallback(Throwable ex) {
        log.error("商品服务熔断降级，服务不可用: {}", ex.getMessage());
        Map<String, Object> result = new HashMap<>();
        result.put("message", "商品服务暂时不可用，请稍后重试");
        result.put("timestamp", System.currentTimeMillis());
        result.put("fallback", true);
        return Result.error("商品服务暂时不可用，请稍后重试");
    }

    public Result<Map<String, Object>> handleOrderServiceBlock(BlockException ex) {
        log.warn("订单服务触发流控，请求被限流: {}", ex.getMessage());
        Map<String, Object> result = new HashMap<>();
        result.put("message", "订单服务繁忙，请稍后重试");
        result.put("timestamp", System.currentTimeMillis());
        result.put("blocked", true);
        return Result.error("订单服务繁忙，请稍后重试");
    }

    public Result<Map<String, Object>> handleOrderServiceFallback(Throwable ex) {
        log.error("订单服务熔断降级，服务不可用: {}", ex.getMessage());
        Map<String, Object> result = new HashMap<>();
        result.put("message", "订单服务暂时不可用，请稍后重试");
        result.put("timestamp", System.currentTimeMillis());
        result.put("fallback", true);
        return Result.error("订单服务暂时不可用，请稍后重试");
    }

    /**
     * 获取Sentinel状态信息
     */
    @GetMapping("/status")
    @ApiOperation("获取Sentinel状态")
    public Result<Map<String, Object>> getSentinelStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("sentinelEnabled", true);
        status.put("timestamp", System.currentTimeMillis());
        status.put("message", "Sentinel集成完成，流量控制和熔断降级功能已启用");
        return Result.success(status);
    }
}
