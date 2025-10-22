package com.coffee.controller.admin;

import com.coffee.result.Result;
import com.coffee.service.ServiceDiscoveryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 服务发现管理控制器
 * 用于测试和管理服务注册发现功能
 */
@RestController
@RequestMapping("/admin/service-discovery")
@Api(tags = "服务发现管理接口")
@Slf4j
public class ServiceDiscoveryController {

    @Autowired
    private ServiceDiscoveryService serviceDiscoveryService;

    @GetMapping("/services")
    @ApiOperation("获取所有注册的服务")
    public Result<List<String>> getAllServices() {
        log.info("获取所有注册的服务");
        List<String> services = serviceDiscoveryService.getAllServiceNames();
        return Result.success(services);
    }

    @GetMapping("/instances/{serviceName}")
    @ApiOperation("获取指定服务的实例信息")
    public Result<String> getServiceInstances(@PathVariable String serviceName) {
        log.info("获取服务 {} 的实例信息", serviceName);
        String serviceInfo = serviceDiscoveryService.getServiceInfo(serviceName);
        return Result.success(serviceInfo);
    }

    @GetMapping("/check/{serviceName}")
    @ApiOperation("检查服务是否可用")
    public Result<Boolean> checkServiceAvailable(@PathVariable String serviceName) {
        log.info("检查服务 {} 是否可用", serviceName);
        boolean available = serviceDiscoveryService.isServiceAvailable(serviceName);
        return Result.success(available);
    }

    @GetMapping("/health")
    @ApiOperation("服务健康检查")
    public Result<String> healthCheck() {
        log.info("执行服务健康检查");
        return Result.success("Coffee Server is healthy and registered to Nacos");
    }

    @GetMapping("/info")
    @ApiOperation("获取当前服务信息")
    public Result<String> getCurrentServiceInfo() {
        log.info("获取当前服务信息");
        String currentService = "coffee-server";
        String serviceInfo = serviceDiscoveryService.getServiceInfo(currentService);
        return Result.success("当前服务信息:\n" + serviceInfo);
    }
}
