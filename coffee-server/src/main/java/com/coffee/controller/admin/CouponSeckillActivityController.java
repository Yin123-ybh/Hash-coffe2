// 文件路径：coffee-server/src/main/java/com/coffee/controller/admin/CouponSeckillActivityController.java
package com.coffee.controller.admin;

import com.coffee.dto.CouponSeckillActivityDTO;
import com.coffee.dto.CouponSeckillPageQueryDTO;
import com.coffee.result.PageResult;
import com.coffee.result.Result;
import com.coffee.service.CouponSeckillActivityService;
import com.coffee.vo.CouponSeckillActivityVO;
import com.coffee.vo.CouponSeckillParticipantVO;
import com.coffee.vo.CouponSeckillStatsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminCouponSeckillActivityController")
@RequestMapping("/admin/coupon-seckill")
@Api(tags = "管理员优惠券秒杀活动相关接口")
@Slf4j
public class CouponSeckillActivityController {

    @Autowired
    private CouponSeckillActivityService couponSeckillActivityService;

    @PostMapping
    @ApiOperation("创建优惠券秒杀活动")
    public Result<Void> createActivity(@RequestBody CouponSeckillActivityDTO activityDTO) {
        log.info("创建优惠券秒杀活动：{}", activityDTO);
        couponSeckillActivityService.createActivity(activityDTO);
        return Result.success();
    }

    @GetMapping
    @ApiOperation("获取优惠券秒杀活动列表")
    public Result<List<CouponSeckillActivityVO>> getActivities() {
        log.info("获取优惠券秒杀活动列表");
        List<CouponSeckillActivityVO> activities = couponSeckillActivityService.getActiveActivities();
        return Result.success(activities);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询优惠券秒杀活动")
    public Result<PageResult<CouponSeckillActivityVO>> getActivitiesPage(CouponSeckillPageQueryDTO pageQueryDTO) {
        log.info("分页查询优惠券秒杀活动：{}", pageQueryDTO);
        PageResult<CouponSeckillActivityVO> pageResult = couponSeckillActivityService.getActivitiesPage(pageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取优惠券秒杀活动详情")
    public Result<CouponSeckillActivityVO> getActivityById(@PathVariable Long id) {
        log.info("获取优惠券秒杀活动详情，活动ID：{}", id);
        CouponSeckillActivityVO activity = couponSeckillActivityService.getActivityById(id);
        return Result.success(activity);
    }

    @PutMapping("/{id}/status/{status}")
    @ApiOperation("更新活动状态")
    public Result<Void> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        log.info("更新活动状态，活动ID：{}，状态：{}", id, status);
        couponSeckillActivityService.updateActivityStatus(id, status);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("更新优惠券秒杀活动")
    public Result<Void> updateActivity(@RequestBody CouponSeckillActivityDTO activityDTO) {
        log.info("更新优惠券秒杀活动：{}", activityDTO);
        couponSeckillActivityService.updateActivity(activityDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除优惠券秒杀活动")
    public Result<Void> deleteActivity(@PathVariable Long id) {
        log.info("删除优惠券秒杀活动，活动ID：{}", id);
        couponSeckillActivityService.deleteActivity(id);
        return Result.success();
    }

    @GetMapping("/{id}/stats")
    @ApiOperation("获取活动统计信息")
    public Result<CouponSeckillStatsVO> getActivityStats(@PathVariable Long id) {
        log.info("获取活动统计信息，活动ID：{}", id);
        CouponSeckillStatsVO stats = couponSeckillActivityService.getActivityStats(id);
        return Result.success(stats);
    }

    @GetMapping("/{id}/participants")
    @ApiOperation("获取活动参与记录")
    public Result<PageResult<CouponSeckillParticipantVO>> getParticipants(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("获取活动参与记录，活动ID：{}，页码：{}，页大小：{}", id, page, pageSize);
        PageResult<CouponSeckillParticipantVO> pageResult = couponSeckillActivityService.getParticipants(id, page, pageSize);
        return Result.success(pageResult);
    }

    @PostMapping("/{id}/warm-up")
    @ApiOperation("预热活动库存")
    public Result<Void> warmUpStock(@PathVariable Long id) {
        log.info("预热活动库存，活动ID：{}", id);
        couponSeckillActivityService.warmUpCouponSeckillStock(id);
        return Result.success();
    }
}