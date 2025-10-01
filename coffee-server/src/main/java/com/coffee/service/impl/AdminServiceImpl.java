package com.coffee.service.impl;

import com.coffee.constant.MessageConstant;
import com.coffee.dto.AdminLoginDTO;
import com.coffee.entity.Admin;
import com.coffee.exception.AccountLockedException;
import com.coffee.exception.AccountNotFoundException;
import com.coffee.exception.PasswordErrorException;
import com.coffee.mapper.AdminMapper;
import com.coffee.properties.JwtProperties;
import com.coffee.service.AdminService;
import com.coffee.utils.JwtUtil;
import com.coffee.vo.AdminLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 管理员登录
     */
    public AdminLoginVO login(AdminLoginDTO adminLoginDTO) {
        String username = adminLoginDTO.getUsername();
        String password = adminLoginDTO.getPassword();
        
        log.info("开始管理员登录验证，用户名：{}", username);

        // 1、根据用户名查询数据库
        Admin admin = adminMapper.getByUsername(username);
        log.info("查询到管理员：{}", admin != null ? admin.getUsername() : "null");

        // 2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (admin == null) {
            //账号不存在
            log.error("管理员账号不存在：{}", username);
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // 对前端传过来的明文密码进行md5加密处理
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        log.info("前端密码加密后：{}", encryptedPassword);
        log.info("数据库密码：{}", admin.getPassword());
        
        if (!encryptedPassword.equals(admin.getPassword())) {
            //密码错误
            log.error("管理员密码错误，用户名：{}", username);
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        //判断账号状态
        if (admin.getStatus() == 0) {
            //账号被锁定
            log.error("管理员账号被锁定，用户名：{}", username);
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        
        log.info("管理员验证通过，开始生成JWT Token");

        // 3、登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("adminId", admin.getId());
        log.info("生成JWT Token，管理员ID：{}", admin.getId());
        
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);
        log.info("JWT Token生成成功：{}", token.substring(0, Math.min(50, token.length())) + "...");

        AdminLoginVO adminLoginVO = AdminLoginVO.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .name(admin.getName())
                .phone(admin.getPhone())
                .sex(admin.getSex())
                .idNumber(admin.getIdNumber())
                .avatar(admin.getAvatar())
                .status(admin.getStatus())
                .token(token)
                .build();

        log.info("管理员登录成功，返回用户信息：{}", admin.getUsername());
        return adminLoginVO;
    }

    /**
     * 根据id查询管理员信息
     */
    public AdminLoginVO getById(Long id) {
        Admin admin = adminMapper.getById(id);
        
        AdminLoginVO adminLoginVO = AdminLoginVO.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .name(admin.getName())
                .phone(admin.getPhone())
                .sex(admin.getSex())
                .idNumber(admin.getIdNumber())
                .avatar(admin.getAvatar())
                .status(admin.getStatus())
                .build();
        return adminLoginVO;
    }
}
