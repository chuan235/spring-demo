package com.gc.viewer.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 配置拦截器的类
 */
//@SpringBootConfiguration
public class ConfigInterceptors extends WebMvcConfigurationSupport {

//    以下WebMvcConfigurerAdapter 比较常用的重写接口
//    /** 解决跨域问题 **/
//    public void addCorsMappings(CorsRegistry registry) ;
//    /** 添加拦截器 **/
//    void addInterceptors(InterceptorRegistry registry);
//    /** 这里配置视图解析器 **/
//    void configureViewResolvers(ViewResolverRegistry registry);
//    /** 配置内容裁决的一些选项 **/
//    void configureContentNegotiation(ContentNegotiationConfigurer configurer);
//    /** 视图跳转控制器 **/
//    void addViewControllers(ViewControllerRegistry registry);
//    /** 静态资源处理 **/
//    void addResourceHandlers(ResourceHandlerRegistry registry);
//    /** 默认静态资源处理器 **/
//    void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer);

    public MyInterceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(MyInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Autowired
    private MyInterceptor interceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                // 添加拦截规则
                .addPathPatterns("/book/**")
                // 排除的路径 可以一直回调
                .excludePathPatterns("/book/login");
        super.addInterceptors(registry);
    }

    /**
     * 配置静态资源，防止被拦截
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**");
        registry.addResourceHandler("/public/**");
        registry.addResourceHandler("/templates/**");
        registry.addResourceHandler("classpath:/static/");
        registry.addResourceHandler("classpath:/templates/");
        registry.addResourceHandler("classpath:/public/");
        super.addResourceHandlers(registry);
    }
}
