package com.coffee.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {
    private Long id;
    private String username;
    private String phone;
    private String nickname;
    private String avatar;
    private Integer gender;
    private Integer points;
    private Integer vipLevel;
    private Integer status;
    private String openid;
    private String name;
    private String sex;
    private String idNumber;
    private String city;
    private String province;
    private String country;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 扩展字段
    private Integer orderCount;
    private Double totalSpent;
    private LocalDateTime lastLoginTime;
}

