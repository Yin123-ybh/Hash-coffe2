package com.coffee.mapper;

import com.coffee.dto.ProductPageQueryDTO;
import com.coffee.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    /**
     * 分页查询商品
     * @param productPageQueryDTO 查询条件
     * @return 商品列表
     */
    List<Product> pageQuery(ProductPageQueryDTO productPageQueryDTO);

    /**
     * 根据ID查询商品
     * @param id 商品ID
     * @return 商品信息
     */
    Product getById(@Param("id") Long id);

    /**
     * 新增商品
     * @param product 商品信息
     */
    void insert(Product product);

    /**
     * 修改商品
     * @param product 商品信息
     */
    void update(Product product);

    /**
     * 根据ID删除商品
     * @param id 商品ID
     */
    void deleteById(@Param("id") Long id);

    /**
     * 批量删除商品
     * @param ids 商品ID列表
     */
    void deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 根据分类ID查询商品数量
     * @param categoryId 分类ID
     * @return 商品数量
     */
    Integer countByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 修改商品状态
     * @param status 状态
     * @param id 商品ID
     */
    void updateStatus(@Param("status") Integer status, @Param("id") Long id);
}