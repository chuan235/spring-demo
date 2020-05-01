package com.gc.bootshiro.web;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class IndexController {

    @RequestMapping({"/","index"})
    public String toIndex(){
        return "index";
    }

    @RequestMapping("/403")
    public String unauthorizedRole(){
        return "403";
    }

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }
    @RequestMapping("login")
    public String login(HttpServletRequest request, Map<String,Object> map) throws Exception{
        System.out.println("shiro开始登录--->>login");
        // 取出登录失败的异常
        // shiroLoginFailure：是shiro异常类的全类名
        String exception = (String)request.getAttribute("shiroLoginFailure");
        String msg = null;
        //判断异常
        if(exception != null){
            // 返回错误信息
            if(UnknownAccountException.class.getName().equals(exception)){
                System.out.println("异常是UnknownAccountException --> 账号不存在");
                msg = "UnknownAccountException --> 账号不存在";
            }else if(IncorrectCredentialsException.class.getName().equals(exception)){
                System.out.println("IncorrectCredentialsException --> 密码错误");
                msg = "IncorrectCredentialsException --> 密码错误";
            }else if("kaptchaValidateFailed".equals(exception)){
                System.out.println("kaptchaValidateFailed --> 验证码错误");
                msg = "kaptchaValidateFailed --> 验证码错误";
            }else{
                msg = "else --> "+exception;
                System.out.println("else -->" + exception);
            }
            map.put("msg",msg);
        }
        // 使用Shiro处理登录
        return "/login";
    }
}
