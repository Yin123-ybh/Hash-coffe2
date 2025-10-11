package com.coffee.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 收货地址实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //用户ID
    private Long userId;

    //收货人姓名
    private String name;

    //收货人电话
    private String phone;

    //省份
    private String province;

    //城市
    private String city;

    //区县
    private String district;

    //详细地址
    private String address;

    //是否默认地址 0-否 1-是
    private Integer isDefault;

    //创建时间
    private LocalDateTime createTime;

    //更新时间
    private LocalDateTime updateTime;
}
