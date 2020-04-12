package top.gmfcj.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppUserConfig {

    /**
     * 使用 @LoadBalanced 会对使用restTemplate访问的服务默认采用轮询访问的机制
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    /**
     * 修改ribbon默认的负载均衡策略
     * @return
     */
//    @Bean
//    public IRule iRule(){
////        return new RoundRobinRule();
//        return new SelfRule();
//    }

}
