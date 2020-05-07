package com.gc.consumer.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.gc.api.service.DemoService;
import com.gc.api.service.IHelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Reference(interfaceClass = IHelloService.class)
    IHelloService helloService;

    @Reference(version = "demo.service.version")
    DemoService demoService;

    @GetMapping("/hello")
    public String hello() {
        helloService.sayHello();
        return "consumer hello .....";
    }

    @GetMapping("/demo/{name}")
    public String demo(@PathVariable("name") String name){
        return demoService.sayHello(name);
    }
}
