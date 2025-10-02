// ProductPageQueryDTO.java - 商品分页查询DTO
package com.coffee.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品分页查询DTO
 */
@Data
public class ProductPageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    //页码
    private int page = 1;

    //每页记录数
    private int pageSize = 10;

    //商品名称
    private String name;

    //分类ID
    private Long categoryId;

    //状态 0-下架 1-上架
    private Integer status;

    //是否热销
    private Integer isHot;

    //是否推荐
    private Integer isRecommend;
}