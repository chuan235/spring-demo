package com.gc.viewer.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * 加载外部的配置文件
 * 在classpath:META-INF/spring.factories里面注册
 * 使用流或者是Loader加载
 */
@Component
public class MyEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private final PropertySourceLoader loader = new PropertiesPropertySourceLoader();
    private final YamlPropertySourceLoader loaderYml = new YamlPropertySourceLoader();


    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // 动态加载配置文件
        /*try(InputStream is = new FileInputStream("E:/logs/abc.properties")){
            // 加载本地的配置文件
            Properties source = new Properties();
            source.load(is);
            // 配置一个propertiesSource对象
            PropertiesPropertySource propertiesSource = new PropertiesPropertySource("my",source);
            // 将propertiesSource对象添加到环境中
            environment.getPropertySources().addLast(propertiesSource);
        }catch (Exception ex){
            ex.printStackTrace();
        }*/

        Resource path = new FileSystemResource("E:/logs/springboot.properties");

        environment.getPropertySources().addLast(loadPs(path));
    }

    private PropertySource<?> loadPs(Resource path){
        if(!path.exists()){
            throw new IllegalArgumentException("Resource " + path + " does not exist");
        }
        try{
            return this.loader.load("my",path).get(0);
            //return this.loaderYml.load("custom-resource",path).get(0);
        }catch (Exception e){
            throw new IllegalStateException("Failed to load yaml configuration from " + path, e);
        }
    }
}
