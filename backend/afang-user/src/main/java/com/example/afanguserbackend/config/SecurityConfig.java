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

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private JwtFilter jwtFilter;
//    @Resource
//    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 禁用 CSRF
        http.csrf(AbstractHttpConfigurer::disable)
                // 启用自定义的 CORS 配置
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> {
                    // 配置需要认证的请求
                    auth.requestMatchers("/websocket/**").permitAll();
                    AUTH_WHITELIST.forEach(path -> auth.requestMatchers(path).permitAll()); // 白名单
                    auth.anyRequest().authenticated();
                })
                // 处理未认证的异常
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
//                            log.info("yc:{}", JSON.toJSONString(authException));
                            response.getWriter().write("{\"error\":\"未认证\"}");
                        })
                )
                // 将自定义 JWT 过滤器添加到过滤器链中
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 配置 CORS 策略
     *
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 允许所有域名
        configuration.addAllowedOriginPattern("*");
        // 允许所有请求方法
        configuration.addAllowedMethod("*");
        // 允许所有请求头
        configuration.addAllowedHeader("*");
        // 允许发送认证信息
        configuration.setAllowCredentials(true);
        // 设置响应暴露的头信息
        configuration.addExposedHeader("*");
        // 设置预检请求的缓存时间
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//      所有的路径都跨域
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
//        密码加密强度3-40
        return new BCryptPasswordEncoder(12);
    }

//    @Bean
//    public JwtFilter jwtFilter() {
//        return new JwtFilter(jwtUtil, redisUtil, userDetailsService);
//    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    //    白名单
    private static final List<String> AUTH_WHITELIST = Arrays.asList(
            "/user/loginUser",
            "/user/registerUser",
            "/user/loginUser",
            "/health/check",
            "/user/sendCodeByEmail",
            "user/validateEmailCode"
    );
}
