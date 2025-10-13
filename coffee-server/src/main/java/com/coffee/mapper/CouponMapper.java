package com.coffee.mapper;

import com.coffee.dto.CouponPageQueryDTO;
import com.coffee.entity.Coupon;
import com.coffee.vo.CouponVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 优惠券数据访问接口
 */
@Mapper
public interface CouponMapper {

    /**
     * 分页查询优惠券
     * @param couponPageQueryDTO 查询条件
     * @return 优惠券列表
     */
    Page<CouponVO> pageQuery(CouponPageQueryDTO couponPageQueryDTO);

    /**
     * 根据ID查询优惠券
     * @param id 优惠券ID
     * @return 优惠券信息
     */
    CouponVO selectById(@Param("id") Long id);

    /**
     * 插入优惠券
     * @param coupon 优惠券信息
     */
    void insert(Coupon coupon);

    /**
     * 更新优惠券
     * @param coupon 优惠券信息
     */
    void update(Coupon coupon);

    /**
     * 根据ID删除优惠券
     * @param id 优惠券ID
     */
    void deleteById(@Param("id") Long id);

    /**
     * 更新优惠券状态
     * @param id 优惠券ID
     * @param status 状态
     */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 获取可用优惠券列表
     * @return 可用优惠券列表
     */
    List<CouponVO> getAvailableCoupons();

    /**
     * 更新优惠券已使用数量
     * @param id 优惠券ID
     * @param usedCount 已使用数量
     */
    void updateUsedCount(@Param("id") Long id, @Param("usedCount") Integer usedCount);
}