package com.coffee.service;

import com.coffee.dto.AdminLoginDTO;
import com.coffee.vo.AdminLoginVO;

public interface AdminService {

    /**
     * 管理员登录
     */
    AdminLoginVO login(AdminLoginDTO adminLoginDTO);

    /**
     * 根据id查询管理员信息
     */
    AdminLoginVO getById(Long id);
}
