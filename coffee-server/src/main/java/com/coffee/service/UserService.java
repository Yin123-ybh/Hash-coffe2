// 文件路径：coffee-server/src/main/java/com/coffee/service/UserService.java
package com.coffee.service;

import com.coffee.dto.UserLoginDTO;
import com.coffee.dto.UserPageQueryDTO;
import com.coffee.result.PageResult;
import com.coffee.vo.UserLoginVO;
import com.coffee.vo.UserVO;

public interface UserService {
    UserLoginVO login(UserLoginDTO userLoginDTO);
    UserLoginVO getUserInfo(Long userId);
    
    /**
     * 分页查询用户
     * @param userPageQueryDTO 查询条件
     * @return 分页结果
     */
    PageResult pageQuery(UserPageQueryDTO userPageQueryDTO);

    /**
     * 根据ID查询用户详情
     * @param id 用户ID
     * @return 用户详情
     */
    UserVO getById(Long id);

    /**
     * 更新用户状态
     * @param id 用户ID
     * @param status 新状态
     */
    void updateStatus(Long id, Integer status);
}