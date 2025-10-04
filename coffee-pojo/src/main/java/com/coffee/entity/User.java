// 文件路径：coffee-pojo/src/main/java/com/coffee/entity/User.java
package com.coffee.entity;

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
public class User implements Serializable {
    private Long id;
    private String username;       // 用户名
    private String phone;          // 手机号
    private String password;       // 密码
    private String openid;         // 微信openid
    private String nickname;       // 用户昵称
    private String avatar;         // 头像URL
    private Integer gender;        // 性别 0-未知 1-男 2-女
    private String birthday;       // 生日
    private Integer points;        // 积分
    private Integer vipLevel;      // VIP等级 0-普通 1-银卡 2-金卡 3-钻石
    private String city;           // 城市
    private String province;       // 省份
    private String country;        // 国家
    private Integer status;        // 状态 0-禁用 1-正常
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}