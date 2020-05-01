package com.gc.bootshiro.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Shiro配置类
 * @author HeadMaster
 */
@Configuration
public class ShiroConfig {

    /**
     * 开启shiro AOP注解支持
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * 配置shiro的过滤器
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 配置登录页
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 配置首页
        shiroFilterFactoryBean.setSuccessUrl("/index");
        // 配置未授权跳转页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        // 定义访问资源的权限类型，对不同的资源设置不同的过滤器,从上往下进行筛选
        Map<String,String> filterChainMap = new LinkedHashMap<>();
        // 静态资源不进行认证  anno表示不需要认证
        filterChainMap.put("/static/**","anon");
        // 前往登录页面不拦截
        filterChainMap.put("/toLogin","anon");
        // 配置登出的认证过滤器
        filterChainMap.put("/logout","logout");
        // authc表示所有的请求都必须认证
        filterChainMap.put("/**","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 配置安全管理器
     * @return
     */
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }

    /**
     * 注册Shiro Realme
     * @return
     */
    @Bean
    public MyShiroRealm myShiroRealm(){
        MyShiroRealm realm = new MyShiroRealm();
        // 引入凭证匹配器
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        return realm;
    }

    /**
     * 配置凭正匹配器
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 使用散列算法 使用MD5加密
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        // 配置散列的次数md5(md5(md5(...)))
        hashedCredentialsMatcher.setHashIterations(1024);
        return hashedCredentialsMatcher;
    }

    /**
     * 配置授权异常处理机制
     * @return
     */
    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver(){
        SimpleMappingExceptionResolver simpleMappingExceptionResolver = new SimpleMappingExceptionResolver();
        Properties mappings = new Properties();
        // 数据库异常处理
        mappings.setProperty("DatabaseException","databaseError");
        // 无权限处理
        mappings.setProperty("UnauthorizedException","403");
        // 将异常处理添加到异常映射器中
        simpleMappingExceptionResolver.setExceptionMappings(mappings);
        // 设置异常默认的视图
        simpleMappingExceptionResolver.setDefaultErrorView("error");
        // 设置异常默认的名称 默认为exception
        simpleMappingExceptionResolver.setExceptionAttribute("ex");
        // 配置错误异常格式
        //simpleMappingExceptionResolver.setWarnLogCategory("MvcLogger");
        return simpleMappingExceptionResolver;
    }
}
