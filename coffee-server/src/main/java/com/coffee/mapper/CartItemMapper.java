package com.coffee.mapper;

import com.coffee.entity.CartItem;
import com.coffee.vo.CartItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 购物车Mapper接口
 */
@Mapper
public interface CartItemMapper {

    /**
     * 根据用户ID查询购物车列表
     */
    List<CartItemVO> selectByUserId(Long userId);

    /**
     * 根据用户ID和商品ID查询购物车项
     */
    CartItem selectByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    /**
     * 插入购物车项
     */
    int insert(CartItem cartItem);

    /**
     * 更新购物车项
     */
    int update(CartItem cartItem);

    /**
     * 根据ID删除购物车项
     */
    int deleteById(Long id);

    /**
     * 根据用户ID和商品ID删除购物车项
     */
    int deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    /**
     * 根据用户ID清空购物车
     */
    int deleteByUserId(Long userId);

    /**
     * 根据用户ID统计购物车商品数量
     */
    int countByUserId(Long userId);
}