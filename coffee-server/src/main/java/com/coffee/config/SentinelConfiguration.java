package com.coffee.config;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Sentinel 配置类
 * 配置流量控制和熔断降级规则
 */
@Configuration
@Slf4j
public class SentinelConfiguration {

    /**
     * 配置Sentinel注解切面
     */
    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

    /**
     * 初始化Sentinel规则
     */
    @PostConstruct
    public void initRules() {
        log.info("========== 初始化Sentinel规则 ==========");
        
        // 初始化流控规则
        initFlowRules();
        
        // 初始化熔断规则
        initDegradeRules();
        
        log.info("========== Sentinel规则初始化完成 ==========");
    }

    /**
     * 初始化流控规则
     */
    private void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>();
        
        // 用户查询接口流控规则
        FlowRule userQueryRule = new FlowRule();
        userQueryRule.setResource("userQuery");
        userQueryRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        userQueryRule.setCount(10); // 每秒最多10个请求
        userQueryRule.setStrategy(RuleConstant.STRATEGY_DIRECT);
        userQueryRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        rules.add(userQueryRule);
        
        // 商品查询接口流控规则
        FlowRule productQueryRule = new FlowRule();
        productQueryRule.setResource("productQuery");
        productQueryRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        productQueryRule.setCount(20); // 每秒最多20个请求
        productQueryRule.setStrategy(RuleConstant.STRATEGY_DIRECT);
        productQueryRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        rules.add(productQueryRule);
        
        // 订单查询接口流控规则
        FlowRule orderQueryRule = new FlowRule();
        orderQueryRule.setResource("orderQuery");
        orderQueryRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        orderQueryRule.setCount(5); // 每秒最多5个请求
        orderQueryRule.setStrategy(RuleConstant.STRATEGY_DIRECT);
        orderQueryRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        rules.add(orderQueryRule);
        
        // 优惠券秒杀接口流控规则
        FlowRule seckillRule = new FlowRule();
        seckillRule.setResource("couponSeckill");
        seckillRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        seckillRule.setCount(100); // 每秒最多100个请求
        seckillRule.setStrategy(RuleConstant.STRATEGY_DIRECT);
        seckillRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        rules.add(seckillRule);
        
        FlowRuleManager.loadRules(rules);
        log.info("流控规则初始化完成，共{}条规则", rules.size());
    }

    /**
     * 初始化熔断规则
     */
    private void initDegradeRules() {
        List<DegradeRule> rules = new ArrayList<>();
        
        // 用户服务熔断规则
        DegradeRule userDegradeRule = new DegradeRule();
        userDegradeRule.setResource("userService");
        userDegradeRule.setGrade(RuleConstant.DEGRADE_GRADE_RT); // 响应时间熔断
        userDegradeRule.setCount(200); // 平均响应时间超过200ms
        userDegradeRule.setTimeWindow(10); // 熔断时长10秒
        userDegradeRule.setMinRequestAmount(5); // 最小请求数
        userDegradeRule.setStatIntervalMs(1000); // 统计时长1秒
        rules.add(userDegradeRule);
        
        // 商品服务熔断规则
        DegradeRule productDegradeRule = new DegradeRule();
        productDegradeRule.setResource("productService");
        productDegradeRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO); // 异常比例熔断
        productDegradeRule.setCount(0.5); // 异常比例超过50%
        productDegradeRule.setTimeWindow(10); // 熔断时长10秒
        productDegradeRule.setMinRequestAmount(5); // 最小请求数
        productDegradeRule.setStatIntervalMs(1000); // 统计时长1秒
        rules.add(productDegradeRule);
        
        // 订单服务熔断规则
        DegradeRule orderDegradeRule = new DegradeRule();
        orderDegradeRule.setResource("orderService");
        orderDegradeRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT); // 异常数熔断
        orderDegradeRule.setCount(5); // 异常数超过5个
        orderDegradeRule.setTimeWindow(10); // 熔断时长10秒
        orderDegradeRule.setMinRequestAmount(5); // 最小请求数
        orderDegradeRule.setStatIntervalMs(1000); // 统计时长1秒
        rules.add(orderDegradeRule);
        
        DegradeRuleManager.loadRules(rules);
        log.info("熔断规则初始化完成，共{}条规则", rules.size());
    }
}
