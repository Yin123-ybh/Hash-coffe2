package com.coffee.handler;

import com.coffee.exception.AccountLockedException;
import com.coffee.exception.AccountNotFoundException;
import com.coffee.exception.BaseException;
import com.coffee.exception.PasswordErrorException;
import com.coffee.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 捕获账号不存在异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(AccountNotFoundException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 捕获密码错误异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(PasswordErrorException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 捕获账号锁定异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(AccountLockedException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

}
