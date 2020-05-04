package com.gc.viewer.exception;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;

/**
 * 自定义的错误页面
 * 步骤：
 *  1、取消Spring Boot 默认的错误页面
 *  2、添加自定义的错误页面
 */
//@Componnt
public class MyExceptionPage implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage p404 = new ErrorPage(HttpStatus.NOT_FOUND,"/404.html");
        ErrorPage p500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/500.html");
        ErrorPage nullPage = new ErrorPage(NullPointerException.class,"/nullPage.html");
        registry.addErrorPages(p404,p500,nullPage);
    }

}
