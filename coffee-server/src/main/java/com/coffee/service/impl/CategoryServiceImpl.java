package com.coffee.service.impl;

import com.coffee.entity.Category;
import com.coffee.mapper.CategoryMapper;
import com.coffee.service.CategoryService;
import com.coffee.vo.CategoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryVO> getEnabledCategories() {
        log.info("获取所有启用的分类");
        List<Category> categories = categoryMapper.getEnabledCategories();
        return convertToVOList(categories);
    }

    @Override
    public CategoryVO getById(Long id) {
        log.info("根据ID查询分类: {}", id);
        Category category = categoryMapper.getById(id);
        if (category != null) {
            return convertToVO(category);
        }
        return null;
    }

    @Override
    public List<CategoryVO> getAllCategories() {
        log.info("获取所有分类");
        List<Category> categories = categoryMapper.getAllCategories();
        return convertToVOList(categories);
    }

    /**
     * 转换为VO对象
     */
    private CategoryVO convertToVO(Category category) {
        CategoryVO categoryVO = new CategoryVO();
        BeanUtils.copyProperties(category, categoryVO);
        categoryVO.setStatusText(category.getStatus() == 1 ? "启用" : "禁用");
        return categoryVO;
    }

    /**
     * 转换为VO列表
     */
    private List<CategoryVO> convertToVOList(List<Category> categories) {
        List<CategoryVO> categoryVOList = new ArrayList<>();
        for (Category category : categories) {
            categoryVOList.add(convertToVO(category));
        }
        return categoryVOList;
    }
}

