package top.gmfcj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @EnableHystrixDashboard 开启自动配置hystrix的仪表盘
 */
@EnableHystrixDashboard
@SpringBootApplication
public class ApplicationDashBordStart {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationDashBordStart.class);
    }
}
