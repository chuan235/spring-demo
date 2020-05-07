package com.gc.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gc.api.service.DemoService;
import org.springframework.beans.factory.annotation.Value;

@Service(version = "demo.service.version")
public class DefaultDemoService implements DemoService {

    @Value("${dubbo.application.name}")
    private String serviceName;

    @Override
    public String sayHello(String name) {
        return String.format("[%s] : Hello, %s", serviceName, name);
    }
}
