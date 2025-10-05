package com.coffee.service.impl;

import com.coffee.dto.CartItemDTO;
import com.coffee.entity.CartItem;
import com.coffee.mapper.CartItemMapper;
import com.coffee.service.CartService;
import com.coffee.vo.CartItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    @Autowired
    private CartItemMapper cartItemMapper;


    @Override
    public List<CartItemVO> getCartList(Long userId) {
        log.info("根据用户ID查询购物车列表，用户ID：{}", userId);
        return cartItemMapper.selectByUserId(userId);
    }

    @Override
    @Transactional
    public void addToCart(Long userId, CartItemDTO cartItemDTO) {
        log.info("添加商品到购物车，用户ID：{}，商品ID：{}，商品数量：{}", userId, cartItemDTO.getProductId(), cartItemDTO.getQuantity());
        //检查购物车中是否已经存在该商品
        CartItem existingItem = cartItemMapper.selectByUserIdAndProductId(userId, cartItemDTO.getProductId());
        if(existingItem!=null){
            //如果存在，更新数量
            existingItem.setQuantity(existingItem.getQuantity()+cartItemDTO.getQuantity());
           existingItem.setUpdateTime(LocalDateTime.now());
            cartItemMapper.update(existingItem);
            log.info("更新购物车商品数量，用户ID：{}，商品ID：{}，商品数量：{}", userId, cartItemDTO.getProductId(), existingItem.getQuantity());
        } else {
            //如果不存在，新增
            CartItem cartItem = CartItem.builder()
                    .userId(userId)
                    .productId(cartItemDTO.getProductId())
                    .quantity(cartItemDTO.getQuantity())
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
            cartItemMapper.insert(cartItem);
            log.info("新增购物车商品，用户ID：{}，商品ID：{}，商品数量：{}", userId, cartItemDTO.getProductId(), cartItemDTO.getQuantity());
        }
    }

    @Override
    @Transactional
    public void updateCartItem(Long userId, CartItemDTO cartItemDTO) {
        log.info("更新购物车商品数量，用户ID：{}，商品ID：{}，商品数量：{}", userId, cartItemDTO.getProductId(), cartItemDTO.getQuantity());
        //检查购物车中是否已经存在该商品
        CartItem existingItem = cartItemMapper.selectByUserIdAndProductId(userId, cartItemDTO.getProductId());
        if(existingItem!=null){
            //如果存在，更新数量
            existingItem.setQuantity(cartItemDTO.getQuantity());
            existingItem.setUpdateTime(LocalDateTime.now());
            cartItemMapper.update(existingItem);
        } else {
            throw new RuntimeException("购物车中不存在该商品");
        }
    }

    @Override
    public void removeFromCart(Long userId, Long productId) {
        log.info("删除购物车商品，用户ID: {}, 商品ID: {}", userId, productId);
        cartItemMapper.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    public void clearCart(Long userId) {
        log.info("清空购物车，用户ID：{}", userId);
        cartItemMapper.deleteByUserId(userId);
    }

    @Override
    public Integer getCartCount(Long userId) {
        log.info("根据用户ID查询购物车商品数量，用户ID：{}", userId);
        return cartItemMapper.countByUserId(userId);
    }
}
