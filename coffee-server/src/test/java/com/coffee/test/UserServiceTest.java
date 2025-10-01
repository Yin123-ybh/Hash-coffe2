package com.coffee.test;

import com.coffee.dto.UserRegisterDTO;
import com.coffee.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testRegister() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("testuser");
        userRegisterDTO.setPhone("13800138000");
        userRegisterDTO.setPassword("123456");
        userRegisterDTO.setNickname("测试用户");
        
        userService.register(userRegisterDTO);
        System.out.println("注册成功！");
    }
}
