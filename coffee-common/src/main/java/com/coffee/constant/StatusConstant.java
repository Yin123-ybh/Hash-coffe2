package com.coffee.constant;

/**
 * 状态常量
 */
public class StatusConstant {

    // 优惠券类型
    public static final Integer COUPON_TYPE_DISCOUNT = 1; // 满减券
    public static final Integer COUPON_TYPE_RATE = 2; // 折扣券

    // 优惠券状态
    public static final Integer COUPON_STATUS_DISABLE = 0; // 禁用
    public static final Integer COUPON_STATUS_ENABLE = 1; // 启用

    // 用户优惠券状态
    public static final Integer USER_COUPON_STATUS_UNUSED = 1; // 未使用
    public static final Integer USER_COUPON_STATUS_USED = 2; // 已使用
    public static final Integer USER_COUPON_STATUS_EXPIRED = 3; // 已过期
}
