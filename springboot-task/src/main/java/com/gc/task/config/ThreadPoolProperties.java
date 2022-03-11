package com.gc.task.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author gouchuan
 */
@Data
@ConfigurationProperties(prefix = "web.pool")
public class ThreadPoolProperties {

    private int coreSize;

    private int maxSize;

    private int queueCapacity;

    private int keepAliveSeconds;

    private String threadPrefix;

}
