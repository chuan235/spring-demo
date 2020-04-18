package com.gc.cache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @EnableCaching 开启缓存自动配置
 * 核心导入了CachingConfigurationSelector类
 *  DefaultJCacheOperationSource
 *  SimpleCacheConfiguration
 *     获取ConcurrentMapCacheManager
 *          存储ConcurrentMapCache，也就是cache
 *              ConcurrentMapCache内部使用ConcurrentHashMap存储数据
 *      RedisCacheManager/CacheManager{
 *          cacheName1,cache{"7399","object..."}
 *          cacheName2,cache{"7499","object..."}
 *          cacheName3,cache{"7566","object..."}
 *          ....
 *      }
 */
@MapperScan(basePackages = "com.gc.cache.mapper")
@SpringBootApplication
@EnableCaching
public class CacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheApplication.class, args);
    }

}
