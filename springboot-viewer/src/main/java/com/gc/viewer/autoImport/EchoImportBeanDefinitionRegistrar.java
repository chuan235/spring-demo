package com.gc.viewer.autoImport;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 判断类的全名与包的名称
 */
public class EchoImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        // 获取注解中的包
        Map<String, Object> data = annotationMetadata.getAnnotationAttributes(EnableEcho.class.getName());
        String[] packArr = (String[]) data.get("packages");
        List<String> packages = Arrays.asList(packArr);
        // 注入一个BeanPostProcessor，在Bean初始化之后的操作
        // 初始化bean的构造器
        BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(EchoBeanPostProcessor.class);
        // 注入属性
        bdb.addPropertyValue("packages",packages);
        // spring管理
        beanDefinitionRegistry.registerBeanDefinition(EchoBeanPostProcessor.class.getName(),bdb.getBeanDefinition());
    }
}
