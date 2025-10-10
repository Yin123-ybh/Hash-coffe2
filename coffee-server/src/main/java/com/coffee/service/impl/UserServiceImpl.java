package com.coffee.service.impl;

import com.coffee.dto.UserLoginDTO;
import com.coffee.dto.UserPageQueryDTO;
import com.coffee.entity.User;
import com.coffee.mapper.UserMapper;
import com.coffee.properties.JwtProperties;
import com.coffee.result.PageResult;
import com.coffee.service.UserService;
import com.coffee.utils.JwtUtil;
import com.coffee.utils.WeChatUtil;
import com.coffee.vo.UserLoginVO;
import com.coffee.vo.UserVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private WeChatUtil weChatUtil;
    
    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        log.info("用户微信登录，code: {}", userLoginDTO.getCode());
        
        // 1. 调用微信API验证code，获取openid
        String openid = weChatUtil.validateWxLogin(userLoginDTO.getCode());
        if (openid == null) {
            log.error("微信登录失败，无法获取openid");
            throw new RuntimeException("微信登录失败，请重试");
        }
        
        log.info("获取到openid: {}", openid);
        
        // 2. 根据openid查询用户
        User user = userMapper.getByOpenid(openid);
        
        if (user == null) {
            // 3. 用户不存在，创建新用户
            log.info("用户不存在，创建新用户");
            user = User.builder()
                .username("wx_user_" + openid.substring(0, Math.min(openid.length(), 8))) // 生成默认用户名
                .phone("") // 微信登录时手机号为空
                .password("") // 微信登录时密码为空
                .openid(openid)
                .nickname(userLoginDTO.getNickname())
                .avatar(userLoginDTO.getAvatar())
                .gender(userLoginDTO.getGender())
                .city(userLoginDTO.getCity())
                .province(userLoginDTO.getProvince())
                .country(userLoginDTO.getCountry())
                .status(1)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
            userMapper.insert(user);
            log.info("新用户创建成功，用户ID: {}", user.getId());
        } else {
            // 4. 用户存在，更新用户信息
            log.info("用户已存在，更新用户信息，用户ID: {}", user.getId());
            user.setNickname(userLoginDTO.getNickname());
            user.setAvatar(userLoginDTO.getAvatar());
            user.setGender(userLoginDTO.getGender());
            user.setCity(userLoginDTO.getCity());
            user.setProvince(userLoginDTO.getProvince());
            user.setCountry(userLoginDTO.getCountry());
            user.setUpdateTime(LocalDateTime.now());
            userMapper.update(user);
        }
        
        // 5. 生成JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("openid", user.getOpenid());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        
        log.info("用户登录成功，用户ID: {}, token生成成功", user.getId());
        
        // 6. 构建返回对象
        return UserLoginVO.builder()
            .id(user.getId())
            .openid(user.getOpenid())
            .nickname(user.getNickname())
            .avatar(user.getAvatar())
            .phone(user.getPhone())
            .gender(user.getGender())
            .city(user.getCity())
            .province(user.getProvince())
            .country(user.getCountry())
            .token(token)
            .build();
    }

    @Override
    public UserLoginVO getUserInfo(Long userId) {
        log.info("获取用户信息，用户ID: {}", userId);
        
        User user = userMapper.getById(userId);
        if (user == null) {
            log.error("用户不存在，用户ID: {}", userId);
            throw new RuntimeException("用户不存在");
        }
        
        return UserLoginVO.builder()
            .id(user.getId())
            .openid(user.getOpenid())
            .nickname(user.getNickname())
            .avatar(user.getAvatar())
            .phone(user.getPhone())
            .gender(user.getGender())
            .city(user.getCity())
            .province(user.getProvince())
            .country(user.getCountry())
            .build();
    }

    @Override
    public PageResult pageQuery(UserPageQueryDTO userPageQueryDTO) {
        PageHelper.startPage(userPageQueryDTO.getPage(), userPageQueryDTO.getPageSize());
        Page<UserVO> page = (Page<UserVO>) userMapper.pageQuery(userPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public UserVO getById(Long id) {
        User user = userMapper.getById(id);
        if (user == null) {
            return null;
        }
        
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        // 这里可以添加额外的统计信息查询
        // userVO.setOrderCount(orderMapper.countByUserId(id));
        // userVO.setTotalSpent(orderMapper.sumAmountByUserId(id));
        
        return userVO;
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        userMapper.updateStatus(id, status);
    }
}
