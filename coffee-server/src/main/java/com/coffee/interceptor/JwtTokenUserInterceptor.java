package com.coffee.interceptor;

import com.coffee.context.BaseContext;
import com.coffee.properties.JwtProperties;
import com.coffee.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT令牌用户拦截器
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;

    /*
      校验jwt
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1.从请求头中获取令牌
        String token = request.getHeader("Authorization");
        log.info("从请求头中获取到的令牌: {}", token);

        //去掉bearer前缀
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        //2.检验令牌
        try {
            log.info("用户端jwt校验: {}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get("userId").toString());
            log.info("从jwt令牌中获取到的用户id: {}", userId);
            BaseContext.setCurrentId(userId);
            //3.校验通过，放行
            return true;
        } catch (Exception e) {
            //4.校验不通过，响应401状态码
            log.error("用户端jwt校验失败: {}", e.getMessage());
            response.setStatus(401);
            return false;

        }


    }
}
