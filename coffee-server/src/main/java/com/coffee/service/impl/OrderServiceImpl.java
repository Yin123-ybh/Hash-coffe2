package com.coffee.service.impl;

import com.coffee.context.BaseContext;
import com.coffee.dto.OrderCreateDTO;
import com.coffee.dto.OrderPageQueryDTO;
import com.coffee.entity.Product;
import com.coffee.mapper.OrderMapper;
import com.coffee.mapper.ProductMapper;
import com.coffee.result.PageResult;
import com.coffee.service.OrderService;
import com.coffee.vo.OrderItemVO;
import com.coffee.vo.OrderVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private ProductMapper productMapper;

    @Override
    public PageResult pageQuery(OrderPageQueryDTO orderPageQueryDTO) {
        // 检查是否是管理端请求（通过userId是否为null判断）
        // 如果userId为null，说明是管理端请求，不设置用户过滤
        // 如果userId不为null，说明是用户端请求，设置用户过滤
        if (orderPageQueryDTO.getUserId() == null) {
            // 管理端请求，不设置用户过滤，显示所有订单
        } else {
            // 用户端请求，设置当前用户ID，确保只查询当前用户的订单
            Long currentUserId = BaseContext.getCurrentId();
            orderPageQueryDTO.setUserId(currentUserId);
        }
        
        PageHelper.startPage(orderPageQueryDTO.getPage(), orderPageQueryDTO.getPageSize());
        Page<OrderVO> page = (Page<OrderVO>) orderMapper.pageQuery(orderPageQueryDTO);
        
        for (OrderVO order : page.getResult()) {
            // 加载订单项
            List<OrderItemVO> items = orderMapper.selectItemsByOrderId(order.getId());
            order.setItems(items);
            order.setStatusText(getStatusText(order.getStatus()));
            order.setPayMethodText(getPayMethodText(order.getPayMethod()));
        }
        
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public OrderVO getById(Long id) {
        OrderVO orderVO = orderMapper.selectById(id);
        if (orderVO != null) {
            List<OrderItemVO> items = orderMapper.selectItemsByOrderId(id);
            orderVO.setItems(items);
            orderVO.setStatusText(getStatusText(orderVO.getStatus()));
            orderVO.setPayMethodText(getPayMethodText(orderVO.getPayMethod()));
        }
        return orderVO;
    }

    @Override
    @Transactional
    public OrderVO createOrder(OrderCreateDTO orderCreateDTO) {
        // 1. 生成订单号
        String orderNo = generateOrderNo();
        
        // 2. 计算订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItemVO> orderItems = new ArrayList<>();
        
        for (OrderCreateDTO.OrderItemDTO itemDTO : orderCreateDTO.getItems()) {
            // 查询商品信息
            Product product = productMapper.getById(itemDTO.getProductId());
            if (product == null) {
                throw new RuntimeException("商品不存在");
            }
            
            // 计算小计
            BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(itemDTO.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
            
            // 创建订单项
            OrderItemVO orderItem = OrderItemVO.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productImage(product.getImage())
                .price(product.getPrice())
                .quantity(itemDTO.getQuantity())
                .totalPrice(itemTotal)
                .build();
            orderItems.add(orderItem);
        }
        
        // 3. 计算优惠金额
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (orderCreateDTO.getUserCouponId() != null) {
            // 这里可以添加优惠券处理逻辑
            // 暂时使用前端传递的totalAmount作为实付金额
            discountAmount = totalAmount.subtract(orderCreateDTO.getTotalAmount());
        }
        
        // 4. 创建订单
        OrderVO orderVO = OrderVO.builder()
            .orderNo(orderNo)
            .userId(BaseContext.getCurrentId())
            .addressId(orderCreateDTO.getAddressId())
            .totalAmount(totalAmount)
            .discountAmount(discountAmount)
            .payAmount(orderCreateDTO.getTotalAmount()) // 使用前端计算的实付金额
            .status(1) // 待支付
            .remark(orderCreateDTO.getRemark())
            .createTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .build();
        
        // 5. 保存订单
        orderMapper.insert(orderVO);
        
        // 6. 保存订单项
        for (OrderItemVO item : orderItems) {
            item.setOrderId(orderVO.getId());
            orderMapper.insertItem(item);
        }
        
        orderVO.setItems(orderItems);
        return orderVO;
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        orderMapper.updateStatus(id, status);
    }

    @Override
    public void cancel(Long id) {
        orderMapper.cancel(id);
    }

    @Override
    public void payOrder(Long id) {

    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        return "CO" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    /**
     * 获取状态文本
     */
    private String getStatusText(Integer status) {
        if (status == null) {
            return "未知";
        }
        String[] statusTexts = {"", "待支付", "已支付", "制作中", "已完成", "已取消"};
        return status >= 1 && status <= 5 ? statusTexts[status] : "未知";
    }

    /**
     * 获取支付方式文本
     */
    private String getPayMethodText(Integer payMethod) {
        if (payMethod == null) {
            return "未支付";
        }
        String[] methodTexts = {"", "微信支付", "支付宝", "余额支付"};
        return payMethod >= 1 && payMethod <= 3 ? methodTexts[payMethod] : "未支付";
    }
}