package top.gmfcj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("top.gmfcj.mapper.*")
public class SpringBootStart {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStart.class, args);
        System.out.println("启动成功");
    }
}
