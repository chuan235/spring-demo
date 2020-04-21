package com.gc.deploy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @GetMapping("/test/{name}")
    public String test(@PathVariable("name") String name){
        return "Test deploy project....."+name;
    }


}
