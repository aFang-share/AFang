package com.example.afanguserbackend.config;

import com.example.afanguserbackend.filter.JwtFilter;
import com.example.afanguserbackend.utils.JwtUtil;
import com.example.afanguserbackend.utils.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security安全配置类
 * 配置JWT认证、跨域处理、权限控制等安全相关功能
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    /**
     * JWT工具类
     */
    @Resource
    private JwtUtil jwtUtil;

    /**
     * Redis工具类
     */
    @Resource
    private RedisUtil redisUtil;

    /**
     * JWT过滤器
     */
    @Resource
    private JwtFilter jwtFilter;

    /**
     * 安全过滤器链配置
     * 配置HTTP安全策略，包括CORS、认证、授权等
     *
     * @param http HTTP安全配置对象
     * @return 配置好的安全过滤器链
     * @throws Exception 配置过程中的异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 禁用CSRF保护（JWT不需要）
        http.csrf(AbstractHttpConfigurer::disable)
                // 启用自定义的CORS配置
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 配置请求授权规则
                .authorizeHttpRequests(auth -> {
                    // WebSocket相关路径允许所有请求
                    auth.requestMatchers("/websocket/**").permitAll();
                    // 白名单路径允许无需认证访问
                    AUTH_WHITELIST.forEach(path -> auth.requestMatchers(path).permitAll());
                    // 其他所有请求都需要认证
                    auth.anyRequest().authenticated();
                })
                // 配置异常处理
                .exceptionHandling(exception -> exception
                        // 认证异常处理
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.error("认证异常: {}", authException.getMessage());
                            // 检查响应是否已提交
                            if (!response.isCommitted()) {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.setContentType("application/json;charset=UTF-8");
                                response.getWriter().write("{\"code\":401,\"message\":\"未认证或登录已过期\",\"data\":null}");
                            }
                        })
                        // 访问拒绝异常处理
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.error("访问拒绝异常: {}", accessDeniedException.getMessage());
                            // 检查响应是否已提交
                            if (!response.isCommitted()) {
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                response.setContentType("application/json;charset=UTF-8");
                                response.getWriter().write("{\"code\":403,\"message\":\"访问被拒绝，权限不足\",\"data\":null}");
                            }
                        })
                )
                // 将自定义JWT过滤器添加到过滤器链中
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 配置CORS策略
     * 允许跨域请求，支持前后端分离架构
     *
     * @return CORS配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 允许所有域名（使用模式匹配避免与allowCredentials冲突）
        configuration.addAllowedOriginPattern("*");
        // 允许所有HTTP方法
        configuration.addAllowedMethod("*");
        // 允许所有请求头
        configuration.addAllowedHeader("*");
        // 允许发送认证信息（Cookie等）
        configuration.setAllowCredentials(true);
        // 暴露给前端的响应头
        configuration.addExposedHeader("*");
        // 预检请求缓存时间（秒）
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 所有路径都应用CORS配置
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * 密码编码器Bean
     * 使用BCrypt算法对密码进行加密存储
     *
     * @return BCrypt密码编码器实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt强度范围4-31，这里使用12提供较高的安全性
        return new BCryptPasswordEncoder(12);
    }

    /**
     * 认证管理器Bean
     * 用于处理用户认证逻辑
     *
     * @param authConfig 认证配置对象
     * @return 认证管理器实例
     * @throws Exception 配置异常
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * 认证白名单路径
     * 这些路径不需要JWT认证即可访问
     */
    private static final List<String> AUTH_WHITELIST = Arrays.asList(
            "/user/loginUser",           // 用户登录
            "/user/registerUser",        // 用户注册
            "/health/check",             // 健康检查
            "/user/sendCodeByEmail",     // 发送邮箱验证码
            "/user/validateEmailCode",   // 验证邮箱验证码
            "/user/validatePhoneCode",   // 验证手机验证码
            "/public/**",                // 公共资源路径
            "/swagger-ui/**",            // Swagger UI
            "/v3/api-docs/**"            // OpenAPI文档
    );
}
