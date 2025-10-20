package com.example.afanguserbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类
 * 配置RedisTemplate的序列化规则，确保数据正确存储和读取
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@Configuration
public class RedisConfig {

    /**
     * 配置RedisTemplate Bean
     * 设置key和value的序列化规则，使用JSON序列化存储对象
     *
     * @param connectionFactory Redis连接工厂
     * @return 配置好的RedisTemplate实例
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 使用JSON序列化器
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        // 设置key的序列化规则为字符串
        template.setKeySerializer(new StringRedisSerializer());
        // 设置value的序列化规则为JSON
        template.setValueSerializer(jsonRedisSerializer);

        // 设置Hash key的序列化规则为字符串
        template.setHashKeySerializer(new StringRedisSerializer());
        // 设置Hash value的序列化规则为JSON
        template.setHashValueSerializer(jsonRedisSerializer);

        // 执行初始化设置
        template.afterPropertiesSet();
        return template;
    }
}
