package com.coffee.controller.user;

import com.coffee.context.BaseContext;
import com.coffee.exception.BaseException;
import com.coffee.result.Result;
import com.coffee.service.UserCouponService;
import com.coffee.vo.UserCouponVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户优惠券控制器
 */
@RestController("userCouponController")
@RequestMapping("/user/coupons")
@Api(tags = "用户优惠券相关接口")
@Slf4j
public class UserCouponController {

    @Autowired
    private UserCouponService userCouponService;

    /**
     * 获取用户优惠券列表
     * @param status 状态（可选）
     * @return 用户优惠券列表
     */
    @GetMapping
    @ApiOperation("获取用户优惠券列表")
    public Result<List<UserCouponVO>> getUserCoupons(@RequestParam(required = false) Integer status) {
        log.info("获取用户优惠券列表，状态：{}", status);
        Long userId = BaseContext.getCurrentId();
        List<UserCouponVO> list = userCouponService.getUserCoupons(userId, status);
        return Result.success(list);
    }

    /**
     * 领取优惠券
     * @param couponId 优惠券ID
     * @return 操作结果
     */
    @PostMapping("/{couponId}/get")
    @ApiOperation("领取优惠券")
    public Result<Void> getCoupon(@PathVariable Long couponId) {
        log.info("领取优惠券，优惠券ID：{}", couponId);
        Long userId = BaseContext.getCurrentId();
        userCouponService.getCoupon(userId, couponId);
        return Result.success();
    }

    /**
     * 使用优惠券
     * @param userCouponId 用户优惠券ID
     * @param orderId 订单ID
     * @return 操作结果
     */
    @PostMapping("/{userCouponId}/use")
    @ApiOperation("使用优惠券")
    public Result<Void> useCoupon(@PathVariable Long userCouponId, @RequestParam Long orderId) {
        log.info("使用优惠券，用户优惠券ID：{}，订单ID：{}", userCouponId, orderId);
        userCouponService.useCoupon(userCouponId, orderId);
        return Result.success();
    }
    /**
     * 根据ID获取用户优惠券详情
     * @param id 用户优惠券ID
     * @return 用户优惠券详情
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID获取用户优惠券详情")
    public Result<UserCouponVO> getUserCouponById(@PathVariable Long id) {
        log.info("根据ID获取用户优惠券详情：{}", id);
        Long userId = BaseContext.getCurrentId();

        // 验证优惠券是否属于当前用户
        UserCouponVO userCoupon = userCouponService.getById(id);
        if (userCoupon == null || !userCoupon.getUserId().equals(userId)) {
            throw new BaseException("优惠券不存在或无权限访问");
        }

        return Result.success(userCoupon);
    }
}