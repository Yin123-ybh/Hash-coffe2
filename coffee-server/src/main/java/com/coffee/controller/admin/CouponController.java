package com.coffee.controller.admin;

import com.coffee.dto.CouponDTO;
import com.coffee.dto.CouponPageQueryDTO;
import com.coffee.result.PageResult;
import com.coffee.result.Result;
import com.coffee.service.CouponService;
import com.coffee.vo.CouponVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 优惠券管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/coupons")
@Api(tags = "优惠券管理")
public class CouponController {

    @Autowired
    private CouponService couponService;

    /**
     * 新增优惠券
     * @param couponDTO 优惠券信息
     * @return 操作结果
     */
    @PostMapping
    @ApiOperation("新增优惠券")
    public Result<Void> add(@Valid @RequestBody CouponDTO couponDTO) {
        log.info("新增优惠券：{}", couponDTO);
        couponService.add(couponDTO);
        return Result.success();
    }

    /**
     * 分页查询优惠券
     * @param couponPageQueryDTO 查询条件
     * @return 分页结果
     */
    @GetMapping("/page")
    @ApiOperation("分页查询优惠券")
    public Result<PageResult> page(CouponPageQueryDTO couponPageQueryDTO) {
        log.info("分页查询优惠券：{}", couponPageQueryDTO);
        PageResult pageResult = couponService.pageQuery(couponPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据ID查询优惠券
     * @param id 优惠券ID
     * @return 优惠券信息
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询优惠券")
    public Result<CouponVO> getById(@PathVariable Long id) {
        log.info("根据ID查询优惠券：{}", id);
        CouponVO couponVO = couponService.getById(id);
        return Result.success(couponVO);
    }

    /**
     * 修改优惠券
     * @param couponDTO 优惠券信息
     * @return 操作结果
     */
    @PutMapping
    @ApiOperation("修改优惠券")
    public Result<Void> update(@Valid @RequestBody CouponDTO couponDTO) {
        log.info("修改优惠券：{}", couponDTO);
        couponService.update(couponDTO);
        return Result.success();
    }

    /**
     * 启用禁用优惠券
     * @param status 状态
     * @param id 优惠券ID
     * @return 操作结果
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用优惠券")
    public Result<Void> startOrStop(@PathVariable Integer status, Long id) {
        log.info("启用禁用优惠券：{},{}", status, id);
        couponService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 根据ID删除优惠券
     * @param id 优惠券ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @ApiOperation("根据ID删除优惠券")
    public Result<Void> deleteById(@PathVariable Long id) {
        log.info("根据ID删除优惠券：{}", id);
        couponService.deleteById(id);
        return Result.success();
    }
}