package com.coffee.service;

import com.coffee.vo.UserCouponVO;

import java.util.List;

/**
 * 用户优惠券业务接口
 */
public interface UserCouponService {

    /**
     * 获取用户优惠券列表
     * @param userId 用户ID
     * @param status 状态（可选）
     * @return 用户优惠券列表
     */
    List<UserCouponVO> getUserCoupons(Long userId, Integer status);

    /**
     * 领取优惠券
     * @param userId 用户ID
     * @param couponId 优惠券ID
     */
    void getCoupon(Long userId, Long couponId);

    /**
     * 使用优惠券
     * @param userCouponId 用户优惠券ID
     * @param orderId 订单ID
     */
    void useCoupon(Long userCouponId, Long orderId);
    /**
     * 根据ID获取用户优惠券详情
     * @param id 用户优惠券ID
     * @return 用户优惠券详情
     */
    UserCouponVO getById(Long id);
}