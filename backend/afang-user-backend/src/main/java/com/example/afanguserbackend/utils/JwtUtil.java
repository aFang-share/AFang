package com.example.afanguserbackend.utils;

import com.example.afanguserbackend.model.entity.user.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * JWT 工具类，用于处理生成、解析和校验
 * @author 18747
 */
@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}") // 从配置中读取密钥
    private String secretKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 获取签名密钥
    private @NotNull Key getSigningKey() {
        try {
            // 对密钥进行 SHA-256 哈希处理
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(secretKey.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(hash, SignatureAlgorithm.HS256.getJcaName());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("无法初始化 JWT 签名密钥", e);
        }
    }

    // 生成 JWT
    public String generateToken(Users userBase) {
        return Jwts.builder()
                .serializeToJsonWith(new JacksonSerializer<>(objectMapper))
                .claim("user", userBase)
                // 设置主题 (用户唯一标识)
                .setSubject(userBase.getPhone())
                // 签发时间
                .setIssuedAt(new Date())
                // 30 分钟过期
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSigningKey())
                .compact();
    }

    // 创建解析器
    private JwtParser createParser() {
        return Jwts.parserBuilder()
                .deserializeJsonWith(new JacksonDeserializer<>(objectMapper))
                .setSigningKey(getSigningKey())
                .build();
    }

    // 从 token 中提取用户信息
    public Users extractUser(String token) {
        try {
            Object userMap = createParser()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("user");
            return objectMapper.convertValue(userMap, Users.class);
        } catch (ExpiredJwtException e) {
//            TODO："JWT异常"异常封装
            throw new RuntimeException("JWT异常");
        } catch (Exception e) {
            throw new RuntimeException("JWT异常");
        }
    }

    // 验证 token 是否有效
    public boolean isTokenValid(String token, @NotNull Users userBase) {
        Users extractedUser = extractUser(token);
        return (extractedUser.getPhone().equals(userBase.getPhone()) && !isTokenExpired(token));
    }

    // 检查 token 是否过期
    public boolean isTokenExpired(String token) {
        return createParser()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

}