package com.gc.viewer.servletapi;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

//@SpringBootConfiguration
public class ServletAPIConfig {

    @Bean
    public ServletRegistrationBean createRegistrationBean(){
        return new ServletRegistrationBean<>(new BookServlet(),"/book/servlet");
    }

    @Bean
    public FilterRegistrationBean createFilterRegistrationBean(){
        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new BookFilter());
        filter.setUrlPatterns(Arrays.asList(new String[]{"/user/*","/book/*"}));
        return filter;
    }

    @Bean
    public ServletListenerRegistrationBean createServletListenerRegistrationBean(){
        return new ServletListenerRegistrationBean<>(new BookListener());
    }
}
