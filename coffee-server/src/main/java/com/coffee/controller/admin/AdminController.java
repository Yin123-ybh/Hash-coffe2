package com.coffee.controller.admin;

import com.coffee.context.BaseContext;
import com.coffee.dto.AdminLoginDTO;
import com.coffee.result.Result;
import com.coffee.service.AdminService;
import com.coffee.vo.AdminLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@Slf4j
@Api(tags = "管理端相关接口")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    @ApiOperation("管理员登录")
    public Result<AdminLoginVO> login(@Valid @RequestBody AdminLoginDTO adminLoginDTO) {
        log.info("管理员登录：{}", adminLoginDTO);
        try {
            AdminLoginVO adminLoginVO = adminService.login(adminLoginDTO);
            log.info("管理员登录成功，返回结果：{}", adminLoginVO != null ? "有数据" : "无数据");
            return Result.success(adminLoginVO);
        } catch (Exception e) {
            log.error("管理员登录异常：", e);
            throw e;
        }
    }

    /**
     * 获取管理员信息
     */
    @GetMapping("/info")
    @ApiOperation("获取管理员信息")
    public Result<AdminLoginVO> info() {
        // 获取当前登录管理员id
        Long adminId = BaseContext.getCurrentId();
        AdminLoginVO adminInfo = adminService.getById(adminId);
        return Result.success(adminInfo);
    }
}
