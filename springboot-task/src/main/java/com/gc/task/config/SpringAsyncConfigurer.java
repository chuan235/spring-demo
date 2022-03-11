package com.gc.task.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;

import java.util.concurrent.Executor;

/**
 * @author gouchuan
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SpringAsyncConfigurer extends AsyncConfigurerSupport {

    private final Executor taskExecutor;

    @Override
    public Executor getAsyncExecutor() {
        return this.taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> log.error(String.format("执行异步任务'%s'", method), ex);
    }
}
