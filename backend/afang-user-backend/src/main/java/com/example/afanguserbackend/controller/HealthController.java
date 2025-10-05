package com.example.afanguserbackend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//@RestController注解表明该类是一个控制器，@RequestMapping注解用于指定该控制器的请求路径
@RestController
//@RequestMapping注解用于指定该控制器的请求路径
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/")
    public String healthCheck() {
        return "ok";
    }
}
