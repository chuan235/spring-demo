package top.gmfcj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.internal.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinServer
public class ApplicationSleuthStart {

    public static void main(String[] args){
        SpringApplication.run(ApplicationSleuthStart.class);
    }
}
