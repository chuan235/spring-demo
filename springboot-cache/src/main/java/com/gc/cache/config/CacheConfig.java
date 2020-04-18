package com.gc.cache.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;

@Configuration
public class CacheConfig {

    @Bean
    public KeyGenerator myKeyGenerator() {

        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... objects) {
                return method.getName()+"-"+ Arrays.toString(objects) +"";
            }
        };
    }


}
