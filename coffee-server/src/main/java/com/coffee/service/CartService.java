package com.coffee.service;

import com.coffee.dto.CartItemDTO;
import com.coffee.vo.CartItemVO;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface CartService {

    /**
     * 获取购物车列表
     */
    List<CartItemVO> getCartList(Long userId);

    /**
     * 添加商品到购物车
     */
    void addToCart(Long userId, CartItemDTO cartItemDTO);

    /**
     * 更新购物车商品数量
     */
    void updateCartItem(Long userId, CartItemDTO cartItemDTO);

    /**
     * 删除购物车商品
     */
    void removeFromCart(Long userId, Long productId);

    /**
     * 清空购物车
     */
    void clearCart(Long userId);

    /**
     * 获取购物车商品数量
     */
    Integer getCartCount(Long userId);
}