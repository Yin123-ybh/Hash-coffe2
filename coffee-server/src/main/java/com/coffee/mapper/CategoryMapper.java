package com.coffee.mapper;

import com.coffee.entity.Category;
import com.coffee.vo.CategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 查询所有启用的分类
     */
    @Select("SELECT * FROM categories WHERE status = 1 ORDER BY sort_order ASC")
    List<Category> getEnabledCategories();

    /**
     * 根据ID查询分类
     */
    @Select("SELECT * FROM categories WHERE id = #{id}")
    Category getById(Long id);

    /**
     * 查询所有分类
     */
    @Select("SELECT * FROM categories ORDER BY sort_order ASC")
    List<Category> getAllCategories();

    /**
     * 插入分类
     */
    int insert(Category category);

    /**
     * 更新分类
     */
    int update(Category category);

    /**
     * 删除分类
     */
    int deleteById(Long id);

    /**
     * 批量删除分类
     */
    int deleteByIds(List<Long> ids);
}
