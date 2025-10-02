// Product.java - 商品实体类
package com.coffee.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //商品名称
    private String name;

    //商品描述
    private String description;

    //分类ID
    private Long categoryId;

    //价格
    private BigDecimal price;

    //原价
    private BigDecimal originalPrice;

    //商品图片
    private String image;

    //商品图片列表(JSON)
    private String images;

    //标签
    private String tags;

    //是否热销 0-否 1-是
    private Integer isHot;

    //是否推荐 0-否 1-是
    private Integer isRecommend;

    //库存
    private Integer stock;

    //销量
    private Integer sales;

    //状态 0-下架 1-上架
    private Integer status;

    //创建时间
    private LocalDateTime createTime;

    //更新时间
    private LocalDateTime updateTime;
}