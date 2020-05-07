package com.gc.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gc.api.service.IHelloService;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = IHelloService.class)
public class HelloServiceImpl implements IHelloService {

    @Override
    public void sayHello() {
        System.out.println("provider say hello.....");
    }
}
