package com.example.afanguserbackend.filter;

import com.example.afanguserbackend.model.entity.user.Users;
import com.example.afanguserbackend.utils.JwtUtil;
import com.example.afanguserbackend.utils.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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
 * JWT 拦截过滤器，用于提取和验证用户身份
 * @author 18747
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UserDetailsService userDetailsService;

    private static final String USER_CACHE_KEY_PREFIX = "users:user:";

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain chain
    ) throws ServletException, IOException {
        // 从请求头中提取 JWT
        String authorizationHeader = request.getHeader("Authorization");


        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // 去掉 Bearer 前缀,获取token
            String jwt = authorizationHeader.substring(7);
            // 解析用户信息
            Users users = jwtUtil.extractUser(jwt);

            if (users != null && redisUtil.hasKey(USER_CACHE_KEY_PREFIX + users.getPhone()) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(users.getPhone());

                if (jwtUtil.isTokenValid(jwt, users)) {
                    // 创建新的认证对象
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 注入到安全上下文中
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } else {
                log.warn("用户未登录或 Redis 缓存过期,Token 无法通过验证: {}", jwt);
            }
        }

        // 继续过滤器链
        chain.doFilter(request, response);
    }
}