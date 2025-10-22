package com.coffee.controller.user;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.coffee.dto.OrderCreateDTO;
import com.coffee.dto.OrderPageQueryDTO;
import com.coffee.result.PageResult;
import com.coffee.result.Result;
import com.coffee.service.OrderService;
import com.coffee.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("userOrderController")
@RequestMapping("/order")
@Api(tags = "用户订单相关接口")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    @ApiOperation("创建订单")
    public Result<OrderVO> createOrder(@Valid @RequestBody OrderCreateDTO orderCreateDTO) {
        log.info("创建订单：{}", orderCreateDTO);
        OrderVO orderVO = orderService.createOrder(orderCreateDTO);
        return Result.success(orderVO);
    }

    @GetMapping("/list")
    @ApiOperation("获取订单列表")
    @SentinelResource(value = "orderQuery", blockHandler = "handleOrderQueryBlock")
    public Result<PageResult> getOrderList(OrderPageQueryDTO orderPageQueryDTO) {
        log.info("获取订单列表：{}", orderPageQueryDTO);
        PageResult pageResult = orderService.pageQuery(orderPageQueryDTO);
        return Result.success(pageResult);
    }
    
    /**
     * 订单查询流控降级处理
     */
    public Result<PageResult> handleOrderQueryBlock(OrderPageQueryDTO orderPageQueryDTO, BlockException ex) {
        log.warn("订单查询触发流控，请求被限流: {}", ex.getMessage());
        return Result.error("系统繁忙，请稍后重试");
    }

    @GetMapping("/{id}")
    @ApiOperation("获取订单详情")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id) {
        log.info("获取订单详情：{}", id);
        OrderVO orderVO = orderService.getById(id);
        return Result.success(orderVO);
    }

    @PutMapping("/{id}/cancel")
    @ApiOperation("取消订单")
    public Result<Void> cancelOrder(@PathVariable Long id) {
        log.info("取消订单：{}", id);
        orderService.cancel(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    @ApiOperation("更新订单状态")
    public Result<Void> updateOrderStatus(@PathVariable Long id, @RequestParam Integer status) {
        log.info("更新订单状态：id={}, status={}", id, status);
        orderService.updateStatus(id, status);
        return Result.success();
    }
}
