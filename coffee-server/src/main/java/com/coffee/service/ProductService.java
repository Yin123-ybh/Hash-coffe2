package com.coffee.service;

import com.coffee.dto.ProductDTO;
import com.coffee.dto.ProductPageQueryDTO;
import com.coffee.result.PageResult;
import com.coffee.vo.ProductVO;

import java.util.List;

public interface ProductService {

    /**
     * 分页查询商品
     * @param productPageQueryDTO 查询条件
     * @return 分页结果
     */
    PageResult pageQuery(ProductPageQueryDTO productPageQueryDTO);

    /**
     * 根据ID查询商品
     * @param id 商品ID
     * @return 商品信息
     */
    ProductVO getById(Long id);

    /**
     * 新增商品
     * @param productDTO 商品信息
     * @return 商品信息
     */
    ProductVO save(ProductDTO productDTO);

    /**
     * 修改商品
     * @param productDTO 商品信息
     * @return 商品信息
     */
    ProductVO update(ProductDTO productDTO);

    /**
     * 删除商品
     * @param id 商品ID
     */
    void deleteById(Long id);

    /**
     * 批量删除商品
     * @param ids 商品ID列表
     */
    void deleteByIds(List<Long> ids);

    /**
     * 启用禁用商品
     * @param status 状态
     * @param id 商品ID
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据分类ID查询商品列表
     * @param categoryId 分类ID
     * @return 商品列表
     */
    List<ProductVO> listByCategoryId(Long categoryId);
}