// 文件路径：coffee-pojo/src/main/java/com/coffee/vo/UserLoginVO.java
package com.coffee.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO implements Serializable {
    private Long id;
    private String openid;
    private String nickname;
    private String avatar;
    private String phone;
    private Integer gender;
    private String city;
    private String province;
    private String country;
    private String token;      // JWT token
}