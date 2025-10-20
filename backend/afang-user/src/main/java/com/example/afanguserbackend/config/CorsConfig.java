package com.example.afanguserbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置类
 * 解决前后端分离项目中的跨域问题
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * 配置跨域映射
     * 允许前端应用跨域访问后端API
     *
     * @param registry 跨域注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 配置所有路径的跨域访问
        registry.addMapping("/**")
                // 允许发送Cookie，用于session认证
                .allowCredentials(true)
                // 允许所有域名访问（使用patterns避免与allowCredentials冲突）
                .allowedOriginPatterns("*")
                // 允许的HTTP方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 允许的请求头
                .allowedHeaders("*")
                // 暴露给前端的响应头
                .exposedHeaders("*");
    }
}