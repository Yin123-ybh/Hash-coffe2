package com.coffee.service;

import java.util.Map;

public interface AiService {
    
    /**
     * AI对话
     * @param params 对话参数
     * @return AI回复
     */
    Map<String, Object> chat(Map<String, Object> params);
}

