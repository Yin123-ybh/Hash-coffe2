package com.coffee.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页查询结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    //总记录数
    private Long total;

    //当前页数据集合
    private List<T> records;
    
    // 添加静态方法用于创建PageResult
    public static <T> PageResult<T> of(Long total, List<T> records) {
        return PageResult.<T>builder()
                .total(total)
                .records(records)
                .build();
    }
}