// 文件路径：coffee-server/src/main/java/com/coffee/mapper/UserMapper.java
package com.coffee.mapper;

import com.coffee.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    // 根据openid查询用户
    User getByOpenid(@Param("openid") String openid);

    // 插入用户
    void insert(User user);

    // 更新用户信息
    void update(User user);

    // 根据id查询用户
    User getById(@Param("id") Long id);
}