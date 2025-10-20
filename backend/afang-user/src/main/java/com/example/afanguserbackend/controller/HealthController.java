package com.example.afanguserbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查控制器
 * 提供系统健康状态检查接口，用于监控服务运行状态
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/health")
@Slf4j
public class HealthController {

    /**
     * 健康检查接口
     * 用于负载均衡器、监控系统等检查服务是否正常运行
     *
     * @return 返回"ok"表示服务正常
     */
    @GetMapping("/check")
    public String healthCheck() {
        // 日志级别从低到高：TRACE < DEBUG < INFO < WARN < ERROR
        // 可根据需要启用不同级别的日志记录
        // log.trace("TRACE 级别日志");
        // log.debug("DEBUG 级别日志");
        // log.info("INFO 级别日志");
        // log.warn("WARN 级别日志");
        // log.error("ERROR 级别日志");

        return "ok";
    }
}
