package com.coffee.utils;

import com.coffee.properties.WeChatProperties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class WeChatUtil {

    private static final Logger log = LoggerFactory.getLogger(WeChatUtil.class);

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 获取微信access_token
     * @return access_token
     */
    public String getAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                + weChatProperties.getAppid() + "&secret=" + weChatProperties.getSecret();
        
        try {
            // 先获取字符串响应
            String responseStr = restTemplate.getForObject(url, String.class);
            log.info("微信API响应：{}", responseStr);
            
            if (responseStr != null) {
                // 手动解析JSON字符串
                Map<String, Object> response = parseJsonString(responseStr);
                if (response != null && response.containsKey("access_token")) {
                    return (String) response.get("access_token");
                }
            }
        } catch (Exception e) {
            log.error("获取access_token失败：{}", e.getMessage());
        }
        return null;
    }

    /**
     * 根据code获取openid
     * @param code 微信登录凭证
     * @return 包含openid的Map
     */
    public Map<String, Object> getOpenidFromWx(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="
                + weChatProperties.getAppid() + "&secret=" + weChatProperties.getSecret()
                + "&js_code=" + code + "&grant_type=authorization_code";
        
        try {
            // 先获取字符串响应
            String responseStr = restTemplate.getForObject(url, String.class);
            log.info("微信API响应：{}", responseStr);
            
            if (responseStr != null) {
                // 手动解析JSON字符串
                Map<String, Object> response = parseJsonString(responseStr);
                if (response != null && response.containsKey("openid")) {
                    log.info("获取openid成功：{}", response.get("openid"));
                    return response;
                } else {
                    log.error("获取openid失败：{}", response);
                }
            }
        } catch (Exception e) {
            log.error("获取openid异常：{}", e.getMessage());
        }
        return null;
    }
    
    /**
     * 解析JSON字符串为Map
     */
    private Map<String, Object> parseJsonString(String jsonStr) {
        try {
            // 使用fastjson解析
            return com.alibaba.fastjson.JSON.parseObject(jsonStr, Map.class);
        } catch (Exception e) {
            log.error("解析JSON失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 验证微信登录是否成功
     * @param code 微信登录凭证
     * @return 成功返回openid，失败返回null
     */
    public String validateWxLogin(String code) {
        Map<String, Object> result = getOpenidFromWx(code);
        if (result != null && result.containsKey("openid")) {
            return (String) result.get("openid");
        }
        return null;
    }
}