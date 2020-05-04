package com.gc.viewer.config;

import com.gc.viewer.conditions.GBKCondition;
import com.gc.viewer.convert.GBKEncodingConvert;
import com.gc.viewer.conditions.UTF8Condition;
import com.gc.viewer.convert.UTF8EncodingConvert;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;

/**
 * @Conditional({UTF8Condition.class,GBKCondition.class}) 多个条件时需要同时满足
 * 既可以配置在方法上，也可以配置在类上
 */
@SpringBootConfiguration
public class EncodingConvertConfiguration {

    @Bean
    @Conditional(UTF8Condition.class)
    @Profile("Development")
    public UTF8EncodingConvert createUTF8EncodingConvert(){
        return new UTF8EncodingConvert();
    }

    @Bean
    @Conditional(GBKCondition.class)
    @Profile("Production")
    public GBKEncodingConvert createGBKEncodingConvert(){
        return new GBKEncodingConvert();
    }
}
