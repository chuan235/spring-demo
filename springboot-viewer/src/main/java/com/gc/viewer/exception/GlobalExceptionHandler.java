package com.gc.viewer.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局的异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = IllegalThreadStateException.class)
    @ResponseBody
    public String error1(IllegalThreadStateException ex){
        return "global Excption "+ ex.getClass().getName();
    }
}
