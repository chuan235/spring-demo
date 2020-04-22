package com.gc.expexcel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gc.expexcel.mapper")
public class ExpexcelApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpexcelApplication.class, args);
    }

}
