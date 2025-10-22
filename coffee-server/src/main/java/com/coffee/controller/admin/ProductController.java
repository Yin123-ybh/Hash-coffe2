package com.coffee.controller.admin;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.coffee.dto.ProductDTO;
import com.coffee.dto.ProductPageQueryDTO;
import com.coffee.result.PageResult;
import com.coffee.result.Result;
import com.coffee.service.ProductService;
import com.coffee.vo.ProductVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 商品管理接口
 */
@RestController("adminProductController")
@RequestMapping("/admin/products")
@Api(tags = "商品管理接口")
@Slf4j
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * 分页查询商品
     * @param productPageQueryDTO 查询条件
     * @return 分页结果
     */
    @GetMapping
    @ApiOperation("分页查询商品")
    @SentinelResource(value = "productQuery", blockHandler = "handleProductQueryBlock")
    public Result<PageResult> page(ProductPageQueryDTO productPageQueryDTO) {
        log.info("分页查询商品: {}", productPageQueryDTO);
        PageResult pageResult = productService.pageQuery(productPageQueryDTO);
        return Result.success(pageResult);
    }
    
    /**
     * 商品查询流控降级处理
     */
    public Result<PageResult> handleProductQueryBlock(ProductPageQueryDTO productPageQueryDTO, BlockException ex) {
        log.warn("商品查询触发流控，请求被限流: {}", ex.getMessage());
        return Result.error("系统繁忙，请稍后重试");
    }

    /**
     * 根据ID查询商品
     * @param id 商品ID
     * @return 商品详情
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询商品")
    public Result<ProductVO> getById(@PathVariable Long id) {
        log.info("根据ID查询商品: {}", id);
        ProductVO productVO = productService.getById(id);
        return Result.success(productVO);
    }

    /**
     * 新增商品
     * @param productDTO 商品信息
     * @return 商品信息
     */
    @PostMapping
    @ApiOperation("新增商品")
    public Result<ProductVO> save(@Valid @RequestBody ProductDTO productDTO) {
        log.info("新增商品: {}", productDTO);
        ProductVO productVO = productService.save(productDTO);
        return Result.success(productVO);
    }

    /**
     * 更新商品
     * @param productDTO 商品信息
     * @return 商品信息
     */
    @PutMapping
    @ApiOperation("更新商品")
    public Result<ProductVO> update(@Valid @RequestBody ProductDTO productDTO) {
        log.info("更新商品: {}", productDTO);
        ProductVO productVO = productService.update(productDTO);
        return Result.success(productVO);
    }

     /**
     * 删除商品
     * @param id 商品ID
     * @return 无
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除商品")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("删除商品: {}", id);
        productService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除商品
     * @param ids 商品ID列表
     * @return 无
     */
    @DeleteMapping
    @ApiOperation("批量删除商品")
    public Result<Void> deleteByIds(@RequestParam List<Long> ids) {
        log.info("批量删除商品: {}", ids);
        productService.deleteByIds(ids);
        return Result.success();
    }

     /**
     * 启用禁用商品
     * @param status 状态
     * @param id 商品ID
     * @return 无
     */
    @PutMapping("/status/{status}")
    @ApiOperation("启用禁用商品")
    public Result<Void> startOrStop(@PathVariable Integer status, @RequestParam Long id) {
        log.info("启用禁用商品: {}, {}", status, id);
        productService.startOrStop(status, id);
        return Result.success();
    }

     /**
     * 根据分类ID查询商品列表
     * @param categoryId 分类ID
     * @return 商品列表
     */
    @GetMapping("/category/{categoryId}")
    @ApiOperation("根据分类ID查询商品列表")
    public Result<List<ProductVO>> listByCategoryId(@PathVariable Long categoryId) {
        log.info("根据分类ID查询商品列表: {}", categoryId);
        List<ProductVO> productVOList = productService.listByCategoryId(categoryId);
        return Result.success(productVOList);
    }
}
