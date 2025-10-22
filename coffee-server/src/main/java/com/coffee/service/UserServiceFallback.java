package com.coffee.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.coffee.dto.UserLoginDTO;
import com.coffee.result.Result;
import com.coffee.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务熔断降级示例
 */
@Service
@Slf4j
public class UserServiceFallback {

    /**
     * 用户登录服务熔断降级
     */
    @SentinelResource(value = "userService", blockHandler = "handleUserServiceBlock", fallback = "handleUserServiceFallback")
    public Result<UserLoginVO> login(UserLoginDTO userLoginDTO) {
        log.info("用户登录服务调用");
        // 模拟服务调用
        try {
            Thread.sleep(100); // 模拟网络延迟
            return Result.success(new UserLoginVO());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("服务调用异常", e);
        }
    }

    /**
     * 用户服务流控降级处理
     */
    public Result<UserLoginVO> handleUserServiceBlock(UserLoginDTO userLoginDTO, BlockException ex) {
        log.warn("用户服务触发流控，请求被限流: {}", ex.getMessage());
        return Result.error("系统繁忙，请稍后重试");
    }

    /**
     * 用户服务熔断降级处理
     */
    public Result<UserLoginVO> handleUserServiceFallback(UserLoginDTO userLoginDTO, Throwable ex) {
        log.error("用户服务熔断降级，服务不可用: {}", ex.getMessage());
        return Result.error("服务暂时不可用，请稍后重试");
    }
}
