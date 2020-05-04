package com.gc.viewer.autoImport;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 注解的作用：接收一个包的名称，输出这个包下面的类名
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EchoImportBeanDefinitionRegistrar.class)
public @interface EnableEcho {

    String[] packages() default {};
}
