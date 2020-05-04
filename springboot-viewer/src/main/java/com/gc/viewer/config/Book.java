package com.gc.viewer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Bean装配的问题
 * SpringBoot的装配规则是根据Application类所在的包位置从上向下扫描
 * 默认只会扫描Application所在包及其子包，不包含同级及下下级包等
 * 扫描指定包需要配置
 *  Application的主类上配置@ComponentScan(basePackages = "com.gc")
 */

@ConfigurationProperties(prefix = "book")
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//@Scope(ConfigurableWebApplicationContext.SCOPE_SESSION)
//@Scope(ConfigurableWebApplicationContext.SCOPE_REQUEST)
//@Controller
//@Service
//@Repository
@Component
public class Book {


    private String author;
    private String name;
    private List<Integer> pages;

    public List<Integer> getPages() {
        return pages;
    }

    public void setPages(List<Integer> pages) {
        this.pages = pages;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
