package com.gc.crud.components;


import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * 自定义ErrorAttributes，携带上附加的数据
 */
@Component
public class MyErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String,Object> map = super.getErrorAttributes(webRequest, includeStackTrace);
        map.put("info","错误信息...");
        // 从request中获取添加的数据 request->0  session->1
        Map<String,Object> ext = (Map<String,Object>) webRequest.getAttribute("ext",0);
        // 放入map中返回
        map.put("ext",ext);
        return map;
    }

    public static void main(String[] args) {
        String s = "8d78869f470951332959580424d4bf4f";
        System.out.println(s.length());
    }
}
