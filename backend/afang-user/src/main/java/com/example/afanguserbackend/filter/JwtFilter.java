package com.example.afanguserbackend.filter;

import com.example.afanguserbackend.model.entity.user.Users;
import com.example.afanguserbackend.utils.JwtUtil;
import com.example.afanguserbackend.utils.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器
 * 拦截HTTP请求，提取JWT Token并验证用户身份，设置Spring Security上下文
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    /**
     * JWT工具类
     */
    private final JwtUtil jwtUtil;

    /**
     * Redis工具类
     */
    private final RedisUtil redisUtil;

    /**
     * 用户详情服务
     */
    private final UserDetailsService userDetailsService;

    /**
     * 用户缓存Key前缀
     */
    private static final String USER_CACHE_KEY_PREFIX = "users:user:";

    /**
     * 构造函数
     *
     * @param jwtUtil JWT工具类实例
     * @param redisUtil Redis工具类实例
     * @param userDetailsService 用户详情服务实例
     */
    public JwtFilter(JwtUtil jwtUtil, RedisUtil redisUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 过滤器核心逻辑
     * 从请求头中提取JWT Token，验证有效性，并设置用户认证信息
     *
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param chain 过滤器链
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain chain
    ) throws ServletException, IOException {
        // 从请求头中提取Authorization信息
        String authorizationHeader = request.getHeader("Authorization");

        // 检查是否包含JWT Token
        if (authorizationHeader != null) {
            // 直接使用完整的header内容作为JWT Token
            String jwt = authorizationHeader;

            // 解析JWT Token中的用户信息
            Users user = jwtUtil.extractUser(jwt);

            log.debug("JWT解析用户信息：{}", user);

            // 验证用户信息和Token有效性
            if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 从数据库加载用户详情
                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getPhone());

                // 验证Token是否有效
                if (jwtUtil.isTokenValid(jwt, user)) {
                    // 创建认证对象
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 将认证信息设置到Spring Security上下文中
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    log.debug("用户认证成功，用户名：{}", user.getPhone());
                } else {
                    log.warn("JWT Token验证失败，用户：{}", user.getPhone());
                }
            } else {
                log.warn("JWT Token无效或用户已认证，Token：{}", jwt);
            }
        }

        // 继续执行过滤器链
        chain.doFilter(request, response);
    }
}