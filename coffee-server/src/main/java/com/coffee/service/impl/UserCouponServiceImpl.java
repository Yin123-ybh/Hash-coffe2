package com.coffee.service.impl;

import com.coffee.constant.MessageConstant;
import com.coffee.constant.StatusConstant;
import com.coffee.entity.UserCoupon;
import com.coffee.exception.BaseException;
import com.coffee.mapper.CouponMapper;
import com.coffee.mapper.UserCouponMapper;
import com.coffee.service.UserCouponService;
import com.coffee.vo.CouponVO;
import com.coffee.vo.UserCouponVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户优惠券业务实现类
 */
@Service
@Slf4j
public class UserCouponServiceImpl implements UserCouponService {

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    /**
     * 获取用户优惠券列表
     * @param userId 用户ID
     * @param status 状态（可选）
     * @return 用户优惠券列表
     */
    @Override
    public List<UserCouponVO> getUserCoupons(Long userId, Integer status) {
        log.info("获取用户优惠券列表，用户ID：{}，状态：{}", userId, status);
        return userCouponMapper.selectByUserIdAndStatus(userId, status);
    }

    /**
     * 领取优惠券
     * @param userId 用户ID
     * @param couponId 优惠券ID
     */
    @Override
    @Transactional
    public void getCoupon(Long userId, Long couponId) {
        log.info("用户领取优惠券，用户ID：{}，优惠券ID：{}", userId, couponId);

        // 1. 检查优惠券是否存在且可用
        CouponVO coupon = couponMapper.selectById(couponId);
        if (coupon == null) {
            throw new BaseException(MessageConstant.COUPON_NOT_FOUND);
        }

        if (coupon.getStatus() != StatusConstant.COUPON_STATUS_ENABLE) {
            throw new BaseException("优惠券已禁用");
        }

        // 2. 检查优惠券是否在有效期内
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getStartTime()) || now.isAfter(coupon.getEndTime())) {
            throw new BaseException("优惠券不在有效期内");
        }

        // 3. 检查优惠券是否还有剩余
        if (coupon.getUsedCount() >= coupon.getTotalCount()) {
            throw new BaseException("优惠券已领完");
        }

        // 4. 检查用户是否已领取过该优惠券
        int count = userCouponMapper.countByUserIdAndCouponId(userId, couponId);
        if (count > 0) {
            throw new BaseException("您已领取过该优惠券");
        }

        // 5. 创建用户优惠券记录
        UserCoupon userCoupon = UserCoupon.builder()
                .userId(userId)
                .couponId(couponId)
                .status(StatusConstant.USER_COUPON_STATUS_UNUSED)
                .getTime(now)
                .build();

        userCouponMapper.insert(userCoupon);

        // 6. 更新优惠券已使用数量
        couponMapper.updateUsedCount(couponId, coupon.getUsedCount() + 1);

        log.info("用户领取优惠券成功，用户ID：{}，优惠券ID：{}", userId, couponId);
    }

    /**
     * 使用优惠券
     * @param userCouponId 用户优惠券ID
     * @param orderId 订单ID
     */
    @Override
    @Transactional
    public void useCoupon(Long userCouponId, Long orderId) {
        log.info("使用优惠券，用户优惠券ID：{}，订单ID：{}", userCouponId, orderId);

        // 1. 检查用户优惠券是否存在
        UserCouponVO userCoupon = userCouponMapper.selectById(userCouponId);
        if (userCoupon == null) {
            throw new BaseException("用户优惠券不存在");
        }

        // 2. 检查优惠券状态
        if (userCoupon.getStatus() != StatusConstant.USER_COUPON_STATUS_UNUSED) {
            throw new BaseException("优惠券已使用或已过期");
        }

        // 3. 检查优惠券是否在有效期内
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(userCoupon.getStartTime()) || now.isAfter(userCoupon.getEndTime())) {
            throw new BaseException("优惠券已过期");
        }

        // 4. 更新用户优惠券状态
        userCouponMapper.updateStatus(userCouponId, StatusConstant.USER_COUPON_STATUS_USED, orderId);

        log.info("使用优惠券成功，用户优惠券ID：{}，订单ID：{}", userCouponId, orderId);
    }
    /**
     * 根据ID获取用户优惠券详情
     * @param id 用户优惠券ID
     * @return 用户优惠券详情
     */
    @Override
    public UserCouponVO getById(Long id) {
        log.info("根据ID获取用户优惠券详情：{}", id);
        return userCouponMapper.selectById(id);
    }
}