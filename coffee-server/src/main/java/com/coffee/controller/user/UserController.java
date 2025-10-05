package com.coffee.controller.user;

import com.coffee.context.BaseContext;
import com.coffee.dto.UserLoginDTO;
import com.coffee.properties.JwtProperties;
import com.coffee.result.Result;
import com.coffee.service.UserService;
import com.coffee.utils.JwtUtil;
import com.coffee.vo.UserLoginVO;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@Api(tags = "用户管理接口")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    @ApiOperation("用户微信登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        try {
            log.info("用户微信登录请求，code: {}", userLoginDTO.getCode());
            UserLoginVO userLoginVO = userService.login(userLoginDTO);
            log.info("用户登录成功，用户ID: {}", userLoginVO.getId());
            return Result.success(userLoginVO);
        } catch (Exception e) {
            log.error("用户登录失败", e);
            return Result.error("登录失败：" + e.getMessage());
        }
    }


    @GetMapping("/info")
    @ApiOperation("获取用户信息")
    public Result<UserLoginVO> getUserInfo() {
        try {
            // 从BaseContext中获取用户ID
            Long userId = BaseContext.getCurrentId();
            log.info("获取用户信息，用户ID: {}", userId);
            UserLoginVO userInfo = userService.getUserInfo(userId);
            return Result.success(userInfo);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return Result.error("获取用户信息失败：" + e.getMessage());
        }
    }

// 删除原来的getCurrentUserId方法
    
    /**
     * 从请求中获取当前用户ID
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            return Long.valueOf(claims.get("userId").toString());
        } catch (Exception e) {
            log.error("解析token失败", e);
            throw new RuntimeException("无效的token");
        }
    }
}
