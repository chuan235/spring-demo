package top.gmfcj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import top.gmfcj.filter.PowerRuleConfig;


/**
 *  @EnableEurekaClient 向eureka上注册服务
 *  @EnableFeignClients 开启Feign服务
 *  @EnableHystrix  开启hystrix的自动配置
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableHystrix
@RibbonClients({
        // 不同的服务配置不能的负载均衡策略
        //@RibbonClient(name="MODULE-ORDER",configuration = OrderRuleConfig.class),
        @RibbonClient(name="MODULE-POWER",configuration = PowerRuleConfig.class)
})
public class ApplicationUserStart {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationUserStart.class);

    }
}
