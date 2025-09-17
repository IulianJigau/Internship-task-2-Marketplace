package com.java.test.junior;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.java.test.junior.mapper")
public class JuniorApplication {

    public static void main(String[] args) {
        SpringApplication.run(JuniorApplication.class, args);
    }

}
