package com.gc.task.controller;

import com.gc.task.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AsyncController {

    @Autowired
    AsyncService asyncService;

    @GetMapping("/hello")
    public String hello(){
        long start,end;
        start = System.currentTimeMillis();
        asyncService.hello();
        end = System.currentTimeMillis();
        // 不开启异步，耗时：5001
        // 开启异步，耗时：18
        System.out.println("耗时："+ (end-start));
        return "success";
    }

    @GetMapping("/test")
    public String test(){
        asyncService.test();
        System.out.println("test ok");
        return "test";
    }
}
