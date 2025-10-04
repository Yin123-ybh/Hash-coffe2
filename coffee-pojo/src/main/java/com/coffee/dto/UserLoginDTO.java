// 文件路径：coffee-pojo/src/main/java/com/coffee/dto/UserLoginDTO.java
package com.coffee.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class UserLoginDTO implements Serializable {
    private String code;        // 微信登录凭证
    private String nickname;   // 用户昵称
    private String avatar;     // 头像URL
    private Integer gender;    // 性别
    private String city;       // 城市
    private String province;   // 省份
    private String country;    // 国家
}