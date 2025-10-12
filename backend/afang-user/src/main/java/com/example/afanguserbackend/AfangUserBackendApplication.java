package com.example.afanguserbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.afanguserbackend.mapper")
public class AfangUserBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AfangUserBackendApplication.class, args);
    }

}
