package com.gc.task.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncExecutionAspectSupport;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gouchuan
 */
@Configuration
@EnableConfigurationProperties(ThreadPoolProperties.class)
@Slf4j
public class ThreadPoolConfiguration {


    /**
     * 配置@Async注解的线程池
     * @return
     */
    @Bean(name= AsyncExecutionAspectSupport.DEFAULT_TASK_EXECUTOR_BEAN_NAME)
    public Executor taskExecutor(ThreadPoolProperties threadPoolProperties){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolProperties.getCoreSize());
        executor.setMaxPoolSize(threadPoolProperties.getMaxSize());
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        executor.setThreadNamePrefix(threadPoolProperties.getThreadPrefix());
        // 如果队列满了，由主线程去执行对应的任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }


}
