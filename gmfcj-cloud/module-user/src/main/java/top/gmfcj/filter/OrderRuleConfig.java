package top.gmfcj.filter;

import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderRuleConfig {

    @Bean
    public IRule iRule(){
        return new SelfRule();
    }
}
