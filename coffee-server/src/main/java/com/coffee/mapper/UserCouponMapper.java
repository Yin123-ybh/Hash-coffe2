package com.coffee.mapper;

import com.coffee.entity.UserCoupon;
import com.coffee.vo.UserCouponVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户优惠券Mapper
 */
@Mapper
public interface UserCouponMapper {

    /**
     * 根据用户ID和状态查询用户优惠券列表
     * @param userId 用户ID
     * @param status 状态
     * @return 用户优惠券列表
     */
    List<UserCouponVO> selectByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 根据用户ID查询所有用户优惠券
     * @param userId 用户ID
     * @return 用户优惠券列表
     */
    List<UserCouponVO> selectByUserId(@Param("userId") Long userId);

    /**
     * 插入用户优惠券
     * @param userCoupon 用户优惠券
     */
    void insert(UserCoupon userCoupon);

    /**
     * 根据ID查询用户优惠券
     * @param id 用户优惠券ID
     * @return 用户优惠券
     */
    UserCouponVO selectById(@Param("id") Long id);

    /**
     * 更新用户优惠券状态
     * @param id 用户优惠券ID
     * @param status 状态
     * @param orderId 订单ID
     */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("orderId") Long orderId);

    /**
     * 检查用户是否已领取该优惠券
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 数量
     */
    int countByUserIdAndCouponId(@Param("userId") Long userId, @Param("couponId") Long couponId);
}