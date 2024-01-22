package com.ices.aigccommunity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.ices.aigccommunity.dao")
@SpringBootApplication
public class AigcCommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(AigcCommunityApplication.class, args);
    }
}
