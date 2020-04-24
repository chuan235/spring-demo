package com.gc.loggingconfig.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration

public class MyMvcConfig {

    @Bean
    public WebMvcAutoConfiguration corsConfiguration(){
        return new WebMvcAutoConfiguration(){

        };
    }


}
