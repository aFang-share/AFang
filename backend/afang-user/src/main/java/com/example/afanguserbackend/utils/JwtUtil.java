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
import lombok.RequiredArgsConstructor;
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
 * JWT (JSON Web Token) 工具类，用于处理JWT令牌的生成、解析和校验。
 * <p>
 * 该工具类提供了JWT令牌的完整生命周期管理功能，包括：
 * <ul>
 *   <li>生成包含用户信息的JWT令牌</li>
 *   <li>从JWT令牌中提取用户信息</li>
 *   <li>验证JWT令牌的有效性</li>
 *   <li>检查JWT令牌是否过期</li>
 * </ul>
 * <p>
 * 使用HS256算法进行签名，令牌默认有效期为30分钟。
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    /**
     * JWT签名密钥，从配置文件中读取。
     * 配置项：${jwt.secret}
     */
    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * Jackson对象映射器，用于JSON序列化和反序列化。
     */
    private final ObjectMapper objectMapper;

    /**
     * 获取JWT签名密钥。
     * <p>
     * 对配置中的密钥进行SHA-256哈希处理，然后使用HS256算法创建签名密钥。
     *
     * @return JWT签名密钥
     * @throws RuntimeException 当无法初始化JWT签名密钥时抛出
     */
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

    /**
     * 生成JWT令牌。
     * <p>
     * 创建包含用户信息的JWT令牌，令牌包含以下信息：
     * <ul>
     *   <li>用户信息（以JSON格式存储在claim中）</li>
     *   <li>主题：用户手机号</li>
     *   <li>签发时间：当前时间</li>
     *   <li>过期时间：当前时间+30分钟</li>
     * </ul>
     *
     * @param userBase 用户对象，包含用户基本信息
     * @return 生成的JWT令牌字符串
     */
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

    /**
     * 创建JWT解析器。
     * <p>
     * 配置JWT解析器，设置JSON反序列化器和签名密钥，用于解析JWT令牌。
     *
     * @return 配置好的JWT解析器
     */
    private JwtParser createParser() {
        return Jwts.parserBuilder()
                .deserializeJsonWith(new JacksonDeserializer<>(objectMapper))
                .setSigningKey(getSigningKey())
                .build();
    }

    /**
     * 从JWT令牌中提取用户信息。
     * <p>
     * 解析JWT令牌并提取其中的用户信息，将用户信息转换为Users对象。
     * 如果令牌过期或格式错误，将抛出运行时异常。
     *
     * @param token JWT令牌字符串
     * @return 从令牌中提取的用户对象
     * @throws RuntimeException 当JWT令牌过期或解析失败时抛出
     */
    public Users extractUser(String token) {
        try {
            Object userMap = createParser()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("user");
            return objectMapper.convertValue(userMap, Users.class);
        } catch (ExpiredJwtException e) {
            log.warn("JWT令牌已过期: {}", e.getMessage());
            throw new RuntimeException("JWT令牌已过期", e);
        } catch (Exception e) {
            log.error("JWT令牌解析失败: {}", e.getMessage());
            throw new RuntimeException("JWT令牌无效", e);
        }
    }

    /**
     * 验证JWT令牌是否有效。
     * <p>
     * 通过以下两个条件验证令牌的有效性：
     * <ul>
     *   <li>令牌中的用户手机号与提供的用户手机号一致</li>
     *   <li>令牌未过期</li>
     * </ul>
     *
     * @param token     JWT令牌字符串
     * @param userBase 用于验证的用户对象
     * @return 如果令牌有效返回true，否则返回false
     */
    public boolean isTokenValid(String token, @NotNull Users userBase) {
        Users extractedUser = extractUser(token);
        return (extractedUser.getPhone().equals(userBase.getPhone()) && !isTokenExpired(token));
    }

    /**
     * 检查JWT令牌是否过期。
     * <p>
     * 解析令牌并检查其过期时间是否早于当前时间。
     *
     * @param token JWT令牌字符串
     * @return 如果令牌已过期返回true，否则返回false
     */
    public boolean isTokenExpired(String token) {
        return createParser()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

}