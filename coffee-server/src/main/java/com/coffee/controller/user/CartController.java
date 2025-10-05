package com.coffee.controller.user;

import com.coffee.context.BaseContext;
import com.coffee.dto.CartItemDTO;
import com.coffee.result.Result;
import com.coffee.service.CartService;
import com.coffee.vo.CartItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/cart")
@Api(tags = "购物车接口")
@Slf4j
public class CartController {
    @Autowired
    private CartService cartService;

    /**
     * 获取购物车列表
     */
    @GetMapping("/list")
    @ApiOperation("获取购物车列表")
    public Result<List<CartItemVO>> getCartList() {
        // 从请求中获取用户ID
        Long userId = BaseContext.getCurrentId();
        log.info("获取购物车列表，用户ID: {}", userId);

        // 调用服务层方法获取购物车列表
        List<CartItemVO> cartList = cartService.getCartList(userId);

        return Result.success(cartList);
    }

    /**
     * 添加商品到购物车
     */
    @PostMapping("/add")
    @ApiOperation("添加商品到购物车")
    public Result<String> addToCart(@RequestBody CartItemDTO cartItemDTO) {
        Long currentId = BaseContext.getCurrentId();
        log.info("添加商品到购物车，用户ID: {}, 商品信息: {},数量: {}", currentId, cartItemDTO, cartItemDTO.getQuantity());
        // 调用服务层方法添加商品到购物车
        cartService.addToCart(currentId, cartItemDTO);

        return Result.success("添加成功");
    }

    /**
     * 更新购物车商品数量
     */
    @PutMapping("/update")
    @ApiOperation("更新购物车商品数量")
    public Result<String> updateCartItem(@RequestBody CartItemDTO cartItemDTO) {
        Long userId = BaseContext.getCurrentId();
        log.info("更新购物车商品数量，用户ID: {}, 商品ID: {}, 数量: {}",
                userId, cartItemDTO.getProductId(), cartItemDTO.getQuantity());

        cartService.updateCartItem(userId, cartItemDTO);
        return Result.success("更新成功");
    }

    /**
     * 删除购物车商品
     */
    @DeleteMapping("/remove/{productId}")
    @ApiOperation("删除购物车商品")
    public Result<String> removeFromCart(@PathVariable Long productId) {
        Long userId = BaseContext.getCurrentId();
        log.info("删除购物车商品，用户ID: {}, 商品ID: {}", userId, productId);

        cartService.removeFromCart(userId, productId);
        return Result.success("删除成功");
    }

    /**
     * 清空购物车
     */
    @DeleteMapping("/clear")
    @ApiOperation("清空购物车")
    public Result<String> clearCart() {
        Long userId = BaseContext.getCurrentId();
        log.info("清空购物车，用户ID: {}", userId);

        cartService.clearCart(userId);
        return Result.success("清空成功");
    }

    /**
     * 获取购物车商品数量
     */
    @GetMapping("/count")
    @ApiOperation("获取购物车商品数量")
    public Result<Integer> getCartCount() {
        Long userId = BaseContext.getCurrentId();
        log.info("获取购物车商品数量，用户ID: {}", userId);

        Integer count = cartService.getCartCount(userId);
        return Result.success(count);
    }
}
