package com.gc.viewer.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/user/home")
    public String toPage() {
        return "login";
    }

    @PostMapping("/user/login")
    public String login(@RequestParam(name = "username", defaultValue = "admin") String username,
                        @RequestParam(name = "password", required = false) String password) {

        System.out.println("username=" + username + ",password=" + password);
        String flag = password != null ? "success" : "fail";
        return flag;
    }


}
