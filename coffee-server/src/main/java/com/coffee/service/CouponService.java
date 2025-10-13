package com.coffee.service;

import com.coffee.dto.CouponDTO;
import com.coffee.dto.CouponPageQueryDTO;
import com.coffee.result.PageResult;
import com.coffee.vo.CouponVO;
import java.util.List;

/**
 * 优惠券业务接口
 */
public interface CouponService {

    /**
     * 新增优惠券
     * @param couponDTO 优惠券信息
     */
    void add(CouponDTO couponDTO);

    /**
     * 分页查询优惠券
     * @param couponPageQueryDTO 查询条件
     * @return 分页结果
     */
    PageResult pageQuery(CouponPageQueryDTO couponPageQueryDTO);

    /**
     * 根据ID查询优惠券
     * @param id 优惠券ID
     * @return 优惠券信息
     */
    CouponVO getById(Long id);

    /**
     * 修改优惠券
     * @param couponDTO 优惠券信息
     */
    void update(CouponDTO couponDTO);

    /**
     * 启用禁用优惠券
     * @param status 状态
     * @param id 优惠券ID
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据ID删除优惠券
     * @param id 优惠券ID
     */
    void deleteById(Long id);
    /**
     * 获取可用优惠券列表（公共接口）
     * @return 可用优惠券列表
     */
    List<CouponVO> getAvailableCoupons();


}