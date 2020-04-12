package top.gmfcj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ApplicationPowerCopyStart {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationPowerCopyStart.class);
    }

}
