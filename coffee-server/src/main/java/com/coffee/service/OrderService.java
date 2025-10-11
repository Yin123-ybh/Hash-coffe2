package com.coffee.service;

import com.coffee.dto.OrderCreateDTO;
import com.coffee.dto.OrderPageQueryDTO;
import com.coffee.result.PageResult;
import com.coffee.vo.OrderVO;

public interface OrderService {

    /**
     * 分页查询订单
     * @param orderPageQueryDTO 查询条件
     * @return 分页结果
     */
    PageResult pageQuery(OrderPageQueryDTO orderPageQueryDTO);

    /**
     * 根据ID查询订单详情
     * @param id 订单ID
     * @return 订单详情
     */
    OrderVO getById(Long id);

    /**
     * 创建订单
     * @param orderCreateDTO 订单创建DTO
     * @return 订单详情
     */
    OrderVO createOrder(OrderCreateDTO orderCreateDTO);

    /**
     * 更新订单状态
     * @param id 订单ID
     * @param status 新状态
     */
    void updateStatus(Long id, Integer status);

    /**
     * 取消订单
     * @param id 订单ID
     */
    void cancel(Long id);
    /**
     * 支付订单
     */
    void payOrder(Long id);
}

