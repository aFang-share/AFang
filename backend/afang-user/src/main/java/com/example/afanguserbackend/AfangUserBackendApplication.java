package com.example.afanguserbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AFang用户管理系统启动类
 * Spring Boot应用程序的入口点，配置MyBatis Mapper扫描
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@SpringBootApplication
@MapperScan("com.example.afanguserbackend.mapper")
public class AfangUserBackendApplication {

    /**
     * 应用程序主方法
     * 启动Spring Boot应用程序
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(AfangUserBackendApplication.class, args);
    }
}
