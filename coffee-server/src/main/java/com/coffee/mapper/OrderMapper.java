package com.coffee.mapper;

import com.coffee.dto.OrderPageQueryDTO;
import com.coffee.vo.OrderItemVO;
import com.coffee.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * 分页查询订单
     * @param orderPageQueryDTO 查询条件
     * @return 订单列表
     */
    List<OrderVO> pageQuery(OrderPageQueryDTO orderPageQueryDTO);

    /**
     * 根据ID查询订单详情
     * @param id 订单ID
     * @return 订单详情
     */
    OrderVO selectById(@Param("id") Long id);

    /**
     * 根据订单ID查询订单项
     * @param orderId 订单ID
     * @return 订单项列表
     */
    List<OrderItemVO> selectItemsByOrderId(@Param("orderId") Long orderId);

    /**
     * 更新订单状态
     * @param id 订单ID
     * @param status 新状态
     */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 取消订单
     * @param id 订单ID
     */
    void cancel(@Param("id") Long id);

    /**
     * 插入订单
     * @param order 订单信息
     */
    void insert(OrderVO order);

    /**
     * 插入订单项
     * @param orderItem 订单项信息
     */
    void insertItem(OrderItemVO orderItem);
    
    /**
     * 统计总订单数
     * @return 总订单数
     */
    Long countTotalOrders();
    
    /**
     * 统计总营业额
     * @return 总营业额
     */
    Double sumTotalRevenue();
    /**
     * 更新订单支付时间
     * @param id 订单ID
     * @param now 当前时间
     */
    void updatePayTime(Long id, LocalDateTime now);
}
