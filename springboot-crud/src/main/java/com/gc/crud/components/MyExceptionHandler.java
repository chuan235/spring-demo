package com.gc.crud.components;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义的异常数据，要将数据放入到request中,并且要配置status
 * 容器中的DefaultErrorAttributes.getErrorAttributes()就是从HttpServletRequest中获取数据
 */
@ControllerAdvice
public class MyExceptionHandler {


    @ExceptionHandler(UserNotExistException.class)
    public String handlerException(HttpServletRequest request,Exception e){
        Map<String,Object> map = new HashMap<>();
        // 设置转发到错误请求的状态码
        request.setAttribute("javax.servlet.error.status_code",500);
        map.put("code","user not exist");
        map.put("message",e.getMessage());
        // 在将map数据放入到request中
        request.setAttribute("ext",map);
        // 转发到error请求
        return "forward:/error";
    }
}
