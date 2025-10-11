package com.coffee.controller.admin;

import com.coffee.result.Result;
import com.coffee.service.CategoryService;
import com.coffee.vo.CategoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理接口
 */
@RestController("adminCategoryController")
@RequestMapping("/admin/categories")
@Api(tags = "分类管理接口")
@Slf4j
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * 获取所有分类
     * @return 分类列表
     */
    @GetMapping
    @ApiOperation("获取所有分类")
    public Result<List<CategoryVO>> getAllCategories() {
        log.info("获取所有分类");
        List<CategoryVO> categories = categoryService.getEnabledCategories();
        return Result.success(categories);
    }

    /**
     * 根据ID查询分类
     * @param id 分类ID
     * @return 分类详情
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询分类")
    public Result<CategoryVO> getById(@PathVariable Long id) {
        log.info("根据ID查询分类: {}", id);
        CategoryVO categoryVO = categoryService.getById(id);
        return Result.success(categoryVO);
    }
}
