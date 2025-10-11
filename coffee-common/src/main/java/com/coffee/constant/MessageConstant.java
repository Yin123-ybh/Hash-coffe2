package com.coffee.constant;

public class MessageConstant {
    public static final String ACCOUNT_NOT_FOUND = "账号不存在";
    public static final String PASSWORD_ERROR = "密码错误";
    public static final String ACCOUNT_LOCKED = "账号被锁定";
    public static final String ALREADY_EXISTS = "已存在";
    
    // 优惠券相关消息
    public static final String COUPON_NOT_FOUND = "优惠券不存在";
    public static final String COUPON_IN_USE = "优惠券已被使用，无法删除";
    public static final String COUPON_TIME_ERROR = "开始时间不能晚于结束时间";
    public static final String COUPON_TIME_INVALID = "优惠券时间无效";
    public static final String COUPON_AMOUNT_ERROR = "满减券的优惠金额必须小于最低消费金额";
    public static final String COUPON_RATE_ERROR = "折扣券必须设置折扣率";
}
