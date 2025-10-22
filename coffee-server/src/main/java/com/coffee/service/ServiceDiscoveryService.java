package com.coffee.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务发现工具类
 * 用于获取服务实例信息
 */
@Service
@Slf4j
public class ServiceDiscoveryService {

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 获取服务实例列表
     * @param serviceName 服务名称
     * @return 服务实例列表
     */
    public List<ServiceInstance> getServiceInstances(String serviceName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        log.info("发现服务 {} 的实例数量: {}", serviceName, instances.size());
        return instances;
    }

    /**
     * 获取服务实例信息
     * @param serviceName 服务名称
     * @return 服务实例信息字符串
     */
    public String getServiceInfo(String serviceName) {
        List<ServiceInstance> instances = getServiceInstances(serviceName);
        if (instances.isEmpty()) {
            return "服务 " + serviceName + " 没有可用实例";
        }

        StringBuilder info = new StringBuilder();
        info.append("服务 ").append(serviceName).append(" 实例信息:\n");
        for (int i = 0; i < instances.size(); i++) {
            ServiceInstance instance = instances.get(i);
            info.append("  实例").append(i + 1).append(": ")
                .append(instance.getHost()).append(":")
                .append(instance.getPort())
                .append(" (权重: ").append(instance.getMetadata().getOrDefault("weight", "1.0")).append(")")
                .append("\n");
        }
        return info.toString();
    }

    /**
     * 检查服务是否可用
     * @param serviceName 服务名称
     * @return 是否可用
     */
    public boolean isServiceAvailable(String serviceName) {
        List<ServiceInstance> instances = getServiceInstances(serviceName);
        return !instances.isEmpty();
    }

    /**
     * 获取所有注册的服务名称
     * @return 服务名称列表
     */
    public List<String> getAllServiceNames() {
        List<String> services = discoveryClient.getServices();
        log.info("当前注册的所有服务: {}", services);
        return services;
    }
}
