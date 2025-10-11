package com.coffee.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户分页查询DTO
 */
@Data
public class UserPageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int page = 1;
    private int pageSize = 10;
    private String keyword;
    private Integer status;
    private String dateRange;
}

