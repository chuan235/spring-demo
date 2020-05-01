package com.gc.bootshiro.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author HeadMaster
 */
@Controller
@RequestMapping("/userInfo")
public class UserInfoController {

    @RequestMapping("/userList")
    @RequiresPermissions("userInfo:view")
    public String list(){
        return "userInfo";
    }

    @RequestMapping("/userAdd")
    @RequiresPermissions("userInfo:add")
    public String add(){
        return "userInfoadd";
    }

    @RequestMapping("/userDel")
    @RequiresPermissions("userInfo:del")
    public String del(){
        return "userInfodel";
    }

}
