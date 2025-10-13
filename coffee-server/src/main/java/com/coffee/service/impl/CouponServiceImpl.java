package com.coffee.service.impl;

import com.coffee.constant.MessageConstant;
import com.coffee.constant.StatusConstant;
import com.coffee.dto.CouponDTO;
import com.coffee.dto.CouponPageQueryDTO;
import com.coffee.entity.Coupon;
import com.coffee.exception.BaseException;
import com.coffee.mapper.CouponMapper;
import com.coffee.result.PageResult;
import com.coffee.service.CouponService;
import com.coffee.vo.CouponVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CouponServiceImpl implements CouponService {
    @Autowired
    private CouponMapper couponMapper;

    /**
     * 新增优惠券
     * @param couponDTO 优惠券信息
     */
    @Override
    public void add(CouponDTO couponDTO) {
        log.info("新增优惠券：{}", couponDTO);
        //1.数据验证
        validateCouponData(couponDTO);

        //2.构建优惠券对象
        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(couponDTO, coupon);

        //3.设置默认值
        coupon.setUsedCount(0);
        coupon.setCreateTime(LocalDateTime.now());
        coupon.setUpdateTime(LocalDateTime.now());
        //4.保存到数据库
        couponMapper.insert(coupon);
        log.info("新增优惠券成功：{}", coupon);
    }

    /**
     * 分页查询优惠券
     * @param couponPageQueryDTO 查询条件
     * @return 分页结果
     */
    @Override
    public PageResult pageQuery(CouponPageQueryDTO couponPageQueryDTO) {
        log.info("分页查询优惠券：{}", couponPageQueryDTO);
        //1.设置分页参数
        PageHelper.startPage(couponPageQueryDTO.getPage(), couponPageQueryDTO.getPageSize());
        //2.执行查询
        Page<CouponVO> page = couponMapper.pageQuery(couponPageQueryDTO);
        //3.构建分页结果
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据ID查询优惠券详情
     * @param id 优惠券ID
     * @return 优惠券详情
     */
    @Override
    public CouponVO getById(Long id) {
        log.info("根据ID查询优惠券：{}", id);

        CouponVO couponVO = couponMapper.selectById(id);
        if (couponVO == null) {
            throw new BaseException(MessageConstant.COUPON_NOT_FOUND);
        }

        return couponVO;
    }

    /**
     * 更新优惠券
     * @param couponDTO 优惠券信息
     */
    @Override
    public void update(CouponDTO couponDTO) {
        log.info("更新优惠券：{}", couponDTO);
        //1.检查优惠券是否存在
        CouponVO existingCoupon = couponMapper.selectById(couponDTO.getId());
        if (existingCoupon == null) {
            throw new BaseException(MessageConstant.COUPON_NOT_FOUND);
        }
        //2.数据验证
        validateCouponData(couponDTO);
        //3.构建优惠券对象
        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(couponDTO, coupon);
        //4.保持原有数据
        coupon.setUsedCount(existingCoupon.getUsedCount());
        coupon.setCreateTime(existingCoupon.getCreateTime());
        coupon.setUpdateTime(LocalDateTime.now());
        //5.更新数据库
        couponMapper.update(coupon);
        log.info("更新优惠券成功：{}", coupon);
    }

    /**
     * 启用禁用优惠券
     * @param status 状态
     * @param id 优惠券ID
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        log.info("启用禁用优惠券：{},{}", status, id);

        // 1. 检查优惠券是否存在
        CouponVO couponVO = couponMapper.selectById(id);
        if (couponVO == null) {
            throw new BaseException(MessageConstant.COUPON_NOT_FOUND);
        }

        // 2. 更新状态
        couponMapper.updateStatus(id, status);

        log.info("优惠券状态更新成功");
    }

    /**
     * 根据ID删除优惠券
     * @param id 优惠券ID
     */
    @Override
    public void deleteById(Long id) {
        log.info("根据ID删除优惠券：{}", id);

        // 1. 检查优惠券是否存在
        CouponVO couponVO = couponMapper.selectById(id);
        if (couponVO == null) {
            throw new BaseException(MessageConstant.COUPON_NOT_FOUND);
        }

        // 2. 检查是否已被使用
        if (couponVO.getUsedCount() > 0) {
            throw new BaseException(MessageConstant.COUPON_IN_USE);
        }

        // 3. 删除优惠券
        couponMapper.deleteById(id);

        log.info("优惠券删除成功");
    }
    /**
     * 获取可用优惠券列表
     * @return 可用优惠券列表
     */
    @Override
    public List<CouponVO> getAvailableCoupons() {
        log.info("获取可用优惠券列表");
        return couponMapper.getAvailableCoupons();  // ✅ 调用Mapper方法
    }

    /**
     * 验证优惠券数据
     * @param couponDTO 优惠券信息
     */
    private void validateCouponData(CouponDTO couponDTO) {
        // 验证时间字段不为空
        if (couponDTO.getStartTime() == null) {
            throw new BaseException(MessageConstant.COUPON_TIME_ERROR);
        }
        if (couponDTO.getEndTime() == null) {
            throw new BaseException(MessageConstant.COUPON_TIME_ERROR);
        }
        
        // 验证时间
        if (couponDTO.getStartTime().isAfter(couponDTO.getEndTime())) {
            throw new BaseException(MessageConstant.COUPON_TIME_INVALID);
        }
        // 验证类型和对应字段
        if (couponDTO.getType() == StatusConstant.COUPON_TYPE_DISCOUNT) {
            // 满减券必须有优惠金额
            if (couponDTO.getDiscountAmount() == null ||
                    couponDTO.getDiscountAmount().compareTo(couponDTO.getMinAmount()) >= 0) {
                throw new BaseException(MessageConstant.COUPON_AMOUNT_ERROR);
            }
        } else if (couponDTO.getType() == StatusConstant.COUPON_TYPE_RATE) {
            // 折扣券必须有折扣率
            if (couponDTO.getDiscountRate() == null) {
                throw new BaseException(MessageConstant.COUPON_RATE_ERROR);
            }
        }
    }

}
