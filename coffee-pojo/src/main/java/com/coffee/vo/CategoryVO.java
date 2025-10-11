// CategoryVO.java - 分类视图对象
package com.coffee.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分类视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //分类名称
    private String name;

    //分类描述
    private String description;

    //分类图标
    private String icon;

    //排序
    private Integer sort;

    //状态 0-禁用 1-启用
    private Integer status;

    //状态文本
    private String statusText;

    //创建时间
    private LocalDateTime createTime;

    //更新时间
    private LocalDateTime updateTime;
}

