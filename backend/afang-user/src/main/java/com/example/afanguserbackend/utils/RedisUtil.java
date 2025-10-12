package com.example.afanguserbackend.utils;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;


/**
 * @author 18747
 */
@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 将指定的键值对存储到 Redis 中
     *
     * @param key 键，用于标识存储的值。
     * @param value 值，要存储的对象。
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 将指定的键值对存储到 Redis 中，并设置其过期时间。
     *
     * @param key 键，用于标识存储的值。
     * @param value 值，要存储的对象。
     * @param timeout 超时时间，以秒为单位，表示该键值对在 Redis 中的存活时间。
     */
    public void set(String key, Object value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    /**
     * 从 Redis 中获取指定键的值。
     *
     * @param key 键，用于标识要获取的值。
     * @return 值的 {@link Optional} 包装对象。如果键不存在或值为 null，则返回一个空的 {@link Optional}。
     */
    public Optional<Object> get(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    /**
     * 从 Redis 中获取指定键对应的字符串值。
     *
     * @param key 键，用于标识要获取的值。
     * @return 如果键存在且对应的值为字符串，则返回字符串值；如果键不存在或对应值不为字符串，则返回 null。
     */
    public String getString(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * 从 Redis 中删除指定键的值。
     *
     * @param key 键，用于标识要删除的值。
     * @return 如果键被成功删除，则返回 {@code true}；否则返回 {@code false}。
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 检查指定的键是否存在于 Redis 中。
     *
     * @param key 键，用于标识要检查的值。
     * @return 如果键存在于 Redis 中，则返回 {@code true}；否则返回 {@code false}。
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置指定键的过期时间。
     *
     * @param key 键，用于标识存储的值。
     * @param timeout 超时时长，指定键的存活时间，使用 {@link Duration} 表示。
     * @return 如果成功设置过期时间，则返回 {@code true}；如果键不存在或设置失败，则返回 {@code false}。
     */
    public Boolean expire(String key, Duration timeout) {
        return redisTemplate.expire(key, timeout);
    }


    /**
     * 为 Redis 中存储的指定键的值执行自增操作。
     *
     * @param key 键，用于标识 Redis 中存储的值。
     * @param delta 增量值，可以是正数或负数。
     * @return 自增操作后的新值。
     */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 获取指定键的剩余过期时间。
     *
     * 如果键不存在或没有设置过期时间，则返回 {@link Duration#ZERO}。
     *
     * @param key 指定的键，用于查询其剩余过期时间。
     * @return 如果键存在且设置了过期时间，则返回剩余的过期时间（以 {@link Duration} 表示）；否则返回 {@link Duration#ZERO}。
     */
    public Duration getExpire(String key) {
        Long expire = redisTemplate.getExpire(key);
        return expire == null ? Duration.ZERO : Duration.ofSeconds(expire);
    }

    /**
     * 根据模式匹配获取 Redis 中的键集合。
     *
     * 使用通配符模式（例如，"user*" 匹配以 "user" 开头的键）从 Redis 中查找所有符合条件的键。
     *
     * @param pattern 键匹配模式，支持通配符表达式，例如："prefix*" 表示匹配以 "prefix" 开头的键。
     * @return 符合匹配模式的键集合。如果没有匹配的键，返回空集合。
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 将一个值从左侧推入指定 Redis 键的列表中。
     *
     * @param key 键，用于标识列表。
     * @param value 值，要添加到列表的对象。
     */
    public void listPush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 移除并返回存储在指定键的列表末尾的元素。
     * 如果列表为空或键不存在，则返回 null。
     *
     * @param key 键，用于标识 Redis 中的目标列表。
     * @return 被移除的元素对象。如果列表为空或键不存在，则返回 null。
     */
    public Object listPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 更新 Redis 中指定的键对应的值。
     *
     * 如果指定的键存在于 Redis 中，则更新其关联的值；如果键不存在，则不执行任何操作。
     *
     * @param key 键，用于标识要更新的值。
     * @param value 值，要更新的新数据。
     * @return 如果更新成功（键存在并成功更新），返回 {@code true}；如果键不存在，返回 {@code false}。
     */
    public Boolean update(String key, Object value) {
        if (Boolean.TRUE.equals(hasKey(key))) {
            redisTemplate.opsForValue().set(key, value);
            return true;
        }
        return false;
    }

    /**
     * 从 Redis 中获取指定键对应的值，并尝试将其转换为目标类型。
     * 如果值不存在或类型不匹配，则返回 null。
     *
     * @param <T> 值的目标类型。
     * @param key 键，用于标识要获取的值。
     * @param type 目标类型的 {@code Class} 对象，用于指定返回值的类型。
     * @return 如果键存在且值类型匹配，则返回目标类型的值；如果键不存在或值类型不匹配，则返回 null。
     */
    public <T> T getValue(String key, @NotNull Class<T> type) {
        // 从 Redis 获取值
        Object value = redisTemplate.opsForValue().get(key);
        // 检查类型是否匹配
        if (type.isInstance(value)) {
            // 返回目标类型
            return type.cast(value);
        }
        // 如果值不存在或类型不匹配，返回 null
        return null;
    }
}
