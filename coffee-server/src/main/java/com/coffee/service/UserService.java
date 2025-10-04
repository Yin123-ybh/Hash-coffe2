// 文件路径：coffee-server/src/main/java/com/coffee/service/UserService.java
package com.coffee.service;

import com.coffee.dto.UserLoginDTO;
import com.coffee.vo.UserLoginVO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserLoginVO login(UserLoginDTO userLoginDTO);
    UserLoginVO getUserInfo(Long userId);
}