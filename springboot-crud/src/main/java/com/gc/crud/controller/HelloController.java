package com.gc.crud.controller;

import com.gc.crud.components.UserNotExistException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(name="test") String param){
        if(param.equals("abc")){
            throw new UserNotExistException();
        }
        return "hello";
    }

}
