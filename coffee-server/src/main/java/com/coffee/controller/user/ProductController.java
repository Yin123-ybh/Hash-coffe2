package com.coffee.controller.user;

import com.coffee.dto.ProductPageQueryDTO;
import com.coffee.result.PageResult;
import com.coffee.result.Result;
import com.coffee.service.CategoryService;
import com.coffee.service.ProductService;
import com.coffee.vo.CategoryVO;
import com.coffee.vo.ProductVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户端商品接口
 */
@RestController("userProductController")
@RequestMapping("/product")
@Api(tags = "用户端商品接口")
public class ProductController {
    
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;

    /**
     * 获取商品分类列表
     * @return 分类列表
     */
    @GetMapping("/categories")
    @ApiOperation("获取商品分类列表")
    public Result<List<CategoryVO>> getCategories() {
        log.info("获取商品分类列表");
        List<CategoryVO> categories = categoryService.getEnabledCategories();
        return Result.success(categories);
    }

    /**
     * 获取商品列表
     * @param categoryId 分类ID（可选）
     * @param page 页码（可选，默认1）
     * @param pageSize 每页大小（可选，默认10）
     * @return 商品列表
     */
    @GetMapping("/list")
    @ApiOperation("获取商品列表")
    public Result<PageResult> getProductList(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("获取商品列表，分类ID: {}, 页码: {}, 每页大小: {}", categoryId, page, pageSize);
        
        ProductPageQueryDTO queryDTO = new ProductPageQueryDTO();
        queryDTO.setCategoryId(categoryId);
        queryDTO.setPage(page);
        queryDTO.setPageSize(pageSize);
        queryDTO.setStatus(1); // 只查询上架商品
        
        PageResult pageResult = productService.pageQuery(queryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据ID获取商品详情
     * @param id 商品ID
     * @return 商品详情
     */
    @GetMapping("/{id}")
    @ApiOperation("获取商品详情")
    public Result<ProductVO> getProductDetail(@PathVariable Long id) {
        log.info("获取商品详情，商品ID: {}", id);
        ProductVO productVO = productService.getById(id);
        return Result.success(productVO);
    }

    /**
     * 根据分类ID获取商品列表
     * @param categoryId 分类ID
     * @return 商品列表
     */
    @GetMapping("/category/{categoryId}")
    @ApiOperation("根据分类ID获取商品列表")
    public Result<List<ProductVO>> getProductsByCategory(@PathVariable Long categoryId) {
        log.info("根据分类ID获取商品列表，分类ID: {}", categoryId);
        List<ProductVO> products = productService.listByCategoryId(categoryId);
        return Result.success(products);
    }
}
