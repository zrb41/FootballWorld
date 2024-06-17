package com.zrb41;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@MapperScan("com.zrb41.mapper")
public class FootballWorldApplication {

    public static void main(String[] args) {
        // 入口函数
        SpringApplication.run(FootballWorldApplication.class, args);
    }

}
