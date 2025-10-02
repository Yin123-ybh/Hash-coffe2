// ProductVO.java - 商品视图对象
package com.coffee.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String description;

    private Long categoryId;

    private String categoryName;  // 分类名称

    private BigDecimal price;

    private BigDecimal originalPrice;

    private String image;

    private String images;

    private String tags;

    private Integer isHot;

    private Integer isRecommend;

    private Integer stock;

    private Integer sales;

    private Integer status;

    private String statusText;  // 状态文本

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}