package com.example.afanguserbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//@RestController注解表明该类是一个控制器，@RequestMapping注解用于指定该控制器的请求路径
@RestController
//@RequestMapping注解用于指定该控制器的请求路径
@RequestMapping("/health")
@Slf4j
public class HealthController {

    @GetMapping("/")
    public String healthCheck() {
        log.trace("TRACE 级别日志");//日志级别从低到高：TRACE < DEBUG < INFO < WARN < ERROR
        log.debug("DEBUG 级别日志");
        log.info("INFO 级别日志");
        log.warn("WARN 级别日志");
        log.error("ERROR 级别日志");
        return "ok";
    }
}
