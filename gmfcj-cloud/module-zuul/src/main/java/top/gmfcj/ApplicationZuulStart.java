package top.gmfcj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @EnableZuulProxy 与 @EnableZuulServer的区别
 */
@EnableZuulProxy
@SpringBootApplication
public class ApplicationZuulStart {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationZuulStart.class);
    }
}
