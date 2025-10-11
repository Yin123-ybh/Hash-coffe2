package com.coffee.controller.admin;

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

@RestController("adminOrderController")
@RequestMapping("/admin/orders")
@Api(tags = "管理端订单相关接口")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    @ApiOperation("获取订单列表")
    public Result<PageResult> getOrderList(OrderPageQueryDTO orderPageQueryDTO) {
        log.info("管理端获取订单列表：{}", orderPageQueryDTO);
        PageResult pageResult = orderService.pageQuery(orderPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取订单详情")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id) {
        log.info("管理端获取订单详情：{}", id);
        OrderVO orderVO = orderService.getById(id);
        return Result.success(orderVO);
    }

    @PutMapping("/{id}/status")
    @ApiOperation("更新订单状态")
    public Result<Void> updateOrderStatus(@PathVariable Long id, @RequestParam Integer status) {
        log.info("管理端更新订单状态：id={}, status={}", id, status);
        orderService.updateStatus(id, status);
        return Result.success();
    }

    @PutMapping("/{id}/cancel")
    @ApiOperation("取消订单")
    public Result<Void> cancelOrder(@PathVariable Long id) {
        log.info("管理端取消订单：{}", id);
        orderService.cancel(id);
        return Result.success();
    }
}
