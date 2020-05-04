package com.gc.viewer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 环境配置
 * 会在test配置文件运行下实例所有的@Bean(这个bean没有@Profile注解)
 * 相当于：spring.profiles.active=test
 */
@Configuration
@Profile("test")
public class EvnConfig {

    @Bean
    public Runnable runnable(){
        System.out.println("===default===runnable=======");
        return () -> {};
    }

    /**
     * 指定开发环境实例化这个bean
     */
    @Bean
    //@Profile("dev")
    public Runnable runnable1() {
        System.out.println("===dev===runnable1=======");
        return () -> {};
    }

    /**
     * 指定生产环境实例化这个bean
     *
     * @return
     */
    @Bean
    //@Profile("prod")
    public Runnable runnable2() {
        System.out.println("===prod===runnable2=======");
        return () -> {};
    }

}
