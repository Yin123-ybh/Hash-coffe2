// 文件路径：coffee-server/src/main/java/com/coffee/controller/user/CouponSeckillController.java
package com.coffee.controller.user;

import com.coffee.context.BaseContext;
import com.coffee.dto.CouponSeckillParticipateDTO;
import com.coffee.result.Result;
import com.coffee.service.CouponSeckillActivityService;
import com.coffee.vo.CouponSeckillActivityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userCouponSeckillController")
@RequestMapping("/user/coupon-seckill")
@Api(tags = "用户优惠券秒杀相关接口")
@Slf4j
public class CouponSeckillController {

    @Autowired
    private CouponSeckillActivityService couponSeckillActivityService;

    @GetMapping("/activities")
    @ApiOperation("获取优惠券秒杀活动列表")
    public Result<List<CouponSeckillActivityVO>> getActivities() {
        log.info("获取优惠券秒杀活动列表");
        List<CouponSeckillActivityVO> activities = couponSeckillActivityService.getActiveActivities();
        return Result.success(activities);
    }

    @GetMapping("/activities/{id}")
    @ApiOperation("获取优惠券秒杀活动详情")
    public Result<CouponSeckillActivityVO> getActivityById(@PathVariable Long id) {
        log.info("获取优惠券秒杀活动详情，活动ID：{}", id);
        CouponSeckillActivityVO activity = couponSeckillActivityService.getActivityById(id);
        return Result.success(activity);
    }

    @PostMapping("/participate")
    @ApiOperation("参与优惠券秒杀")
    public Result<Void> participateSeckill(@RequestBody CouponSeckillParticipateDTO participateDTO) {
        log.info("用户参与优惠券秒杀：{}", participateDTO);
        Long userId = BaseContext.getCurrentId();
        couponSeckillActivityService.participateSeckill(userId, participateDTO);
        return Result.success();
    }
}