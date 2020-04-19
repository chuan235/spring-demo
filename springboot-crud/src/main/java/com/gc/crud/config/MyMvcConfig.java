package com.gc.crud.config;

import com.gc.crud.components.LoginHandlerInterceptor;
import com.gc.crud.components.MyLocalResolver;
import com.gc.crud.filters.MyFilter;
import com.gc.crud.listeners.MyListener;
import com.gc.crud.servlets.Myservlet;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * 使用WebMvcConfigurer接口的默认方法来扩展SpringMVC
 */
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    /**
     * 配置视图映射
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController("/main.html").setViewName("emp/dashboard");
    }

    /**
     * 判断是那一个区域，生成对应的区域解析器
     * @return 地区解析器
     */
    @Bean
    public LocaleResolver localeResolver(){
        return new MyLocalResolver();
    }

    /**
     * 注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/index.html","/","/user/login");
    }


    // 注册Servlet
    @Bean
    public ServletRegistrationBean myServlet(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        registrationBean.setServlet(new Myservlet());
        registrationBean.addUrlMappings("/myservlet");
        return registrationBean;
    }

    // 注册Filter
    @Bean
    public FilterRegistrationBean myFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new MyFilter());
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/hello","/myservlet"));
        return filterRegistrationBean;
    }

    // 注册Listener
    @Bean
    public ServletListenerRegistrationBean myListener(){
        ServletListenerRegistrationBean listenerRegistrationBean = new ServletListenerRegistrationBean();
        listenerRegistrationBean.setListener(new MyListener());
        return listenerRegistrationBean;
    }

}
