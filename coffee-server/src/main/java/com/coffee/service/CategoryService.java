package com.coffee.service;

import com.coffee.vo.CategoryVO;

import java.util.List;

public interface CategoryService {

    /**
     * 获取所有启用的分类
     * @return 分类列表
     */
    List<CategoryVO> getEnabledCategories();

    /**
     * 根据ID查询分类
     * @param id 分类ID
     * @return 分类详情
     */
    CategoryVO getById(Long id);

    /**
     * 获取所有分类
     * @return 分类列表
     */
    List<CategoryVO> getAllCategories();
}

