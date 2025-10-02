// ProductDTO.java - 商品数据传输对象
package com.coffee.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品DTO
 */
@Data
public class ProductDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "商品名称不能为空")
    @Size(max = 100, message = "商品名称长度不能超过100个字符")
    private String name;

    @Size(max = 500, message = "商品描述长度不能超过500个字符")
    private String description;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private BigDecimal price;

    @DecimalMin(value = "0.01", message = "原价必须大于0")
    private BigDecimal originalPrice;

    private String image;

    private String images;

    private String tags;

    private Integer isHot;

    private Integer isRecommend;

    @Min(value = 0, message = "库存不能小于0")
    private Integer stock;

    private Integer status;
}