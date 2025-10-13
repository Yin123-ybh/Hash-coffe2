package com.coffee.controller;

import com.coffee.result.Result;
import com.coffee.service.CouponService;
import com.coffee.vo.CouponVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公共接口控制器
 */
@RestController
@RequestMapping("/public")
@Api(tags = "公共接口")
@Slf4j
public class PublicController {

    @Autowired
    private CouponService couponService;

    /**
     * 获取可用优惠券列表（公共接口）
     * @return 可用优惠券列表
     */
    @GetMapping("/coupons/available")
    @ApiOperation("获取可用优惠券列表")
    public Result<List<CouponVO>> getAvailableCoupons() {
        log.info("获取可用优惠券列表");
        List<CouponVO> list = couponService.getAvailableCoupons();
        return Result.success(list);
    }
}
