package com.coffee.mapper;

import com.coffee.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMapper {

    /**
     * 根据用户名查询管理员
     */
    Admin getByUsername(@Param("username") String username);

    /**
     * 根据id查询管理员
     */
    Admin getById(@Param("id") Long id);
}
