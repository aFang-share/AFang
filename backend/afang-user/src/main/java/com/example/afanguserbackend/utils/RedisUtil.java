package com.example.afanguserbackend.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 *
 * @author @yue
 */
@Component
public class RedisUtil {

    private static RedisTemplate<String, Object> redisTemplate;

    /**
     * Spring 依赖注入构造器
     *
     * @param redisTemplate Spring 容器中的 RedisTemplate 实例
     */
    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    /**
     * 存储键值对，无过期时间
     *
     * @param key   存储键
     * @param value 存储值
     */
    public static <T> void set(@NotNull String key, @Nullable T value) {
        if (value != null) {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 存储键值对并设置过期时间
     *
     * @param key     存储键
     * @param value   存储值
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    public static <T> void set(@NotNull String key, @Nullable T value, long timeout, @NotNull TimeUnit unit) {
        if (value != null) {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
        }
    }

    /**
     * 存储键值对并设置过期时间
     *
     * @param key     存储键
     * @param value   存储值
     * @param timeout 过期时间-Duration
     */
    public static <T> void set(@NotNull String key, @Nullable T value, @NotNull Duration timeout) {
        set(key, value, timeout.toSeconds(), TimeUnit.SECONDS);
    }
    /**
     * 存储键值对并设置过期时间
     *
     * @param key     存储键
     * @param value   存储值
     * @param timeout 过期时间
     */
    public static <T> void set(@NotNull String key, @NotNull T value, long timeout) {
        set(key, value, timeout, TimeUnit.SECONDS);
    }
    /**
     * 仅当键不存在时存储
     *
     * @param key   存储键
     * @param value 存储值
     * @return 键不存在且存储成功返回 true，否则返回 false
     */
    public static <T> boolean setIfAbsent(@NotNull String key, @Nullable T value) {
        if (value != null) {
            return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value));
        }
        return false;
    }

    /**
     * 获取值并转换为指定类型
     *
     * @param key  存储键
     * @param type 目标类型的 Class 对象
     * @return 包装了目标类型值的 Optional，不存在为空 Optional
     */
    public static <T> Optional<T> get(@NotNull String key, @NotNull Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        return type.isInstance(value) ? Optional.of(type.cast(value)) : Optional.empty();
    }

    /**
     * 快捷获取字符串值
     *
     * @param key 存储键
     * @return 包装了字符串值的 Optional，不存在为空 Optional
     */
    public static Optional<String> getString(@NotNull String key) {
        return get(key, String.class);
    }

    /**
     * 批量获取多个键的值
     *
     * @param keys 键集合
     * @return 与键顺序对应的 值列表，否咋为null
     */
    public static List<Object> multiGet(@NotNull Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 删除指定键
     *
     * @param key 要删除的键
     * @return 删除成功返回 true，键不存在返回 false
     */
    public static boolean delete(@NotNull String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 批量删除键
     *
     * @param keys 要删除的键集合
     * @return 成功删除的键数量
     */
    public static long deleteBatch(@NotNull Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 检查键是否存在
     *
     * @param key 要检查的键
     * @return 存在返回 true，否则返回 false
     */
    public static boolean hasKey(@NotNull String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置键的过期时间
     *
     * @param key     要设置的键
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return 设置成功返回 true，键不存在返回 false
     */
    public static boolean expire(@NotNull String key, long timeout, @NotNull TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置键的过期时间
     *
     * @param key     要设置的键
     * @param timeout 过期时间-Duration
     * @return 设置成功返回 true，键不存在返回 false
     */
    public static boolean expire(@NotNull String key, @NotNull Duration timeout) {
        return expire(key, timeout.toSeconds(), TimeUnit.SECONDS);
    }

    /**
     * 移除键的过期时间（使其永久有效）
     *
     * @param key 要操作的键
     * @return 移除成功返回 true，键不存在返回 false
     */
    public static boolean persist(@NotNull String key) {
        return redisTemplate.persist(key);
    }

    /**
     * 获取键的剩余过期时间
     *
     * @param key 要查询的键
     * @return 剩余时间：秒，不存在为0
     */
    public static long getExpireSeconds(@NotNull String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 获取键的剩余过期时间
     *
     * @param key 要查询的键
     * @return 剩余时间，不存在或已过期返回-Duration.ZERO
     */
    public static Duration getExpire(@NotNull String key) {
        return Duration.ofSeconds(getExpireSeconds(key));
    }

    /**
     * 模糊匹配查询键
     *
     * @param pattern 匹配模式
     * @return 匹配到的键集合，无匹配时返回空集合
     */
    public static @NotNull Set<String> keys(@NotNull String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 为键对应的值执行自增操作（支持整数）
     *
     * @param key   要操作的键
     * @param delta 增量
     * @return 自增后的结果
     */
    public static long increment(@NotNull String key, long delta) {
        Long result = redisTemplate.opsForValue().increment(key, delta);
        return result == null ? 0 : result;
    }

    /**
     * 为键对应的值执行自增操作（支持浮点数）
     *
     * @param key   要操作的键
     * @param delta 增量
     * @return 自增后的结果
     */
    public static double increment(@NotNull String key, double delta) {
        Double result = redisTemplate.opsForValue().increment(key, delta);
        return result == null ? 0 : result;
    }

    /**
     * 向列表左侧添加元素
     *
     * @param key   列表键
     * @param value 要添加的元素
     * @return 操作后列表的长度
     */
    public static <T> long listPush(@NotNull String key, @Nullable T value) {
        Long size = null;
        if (value != null) {
            size = redisTemplate.opsForList().leftPush(key, value);
        }
        return size == null ? 0 : size;
    }

    /**
     * 向列表右侧添加元素
     *
     * @param key   列表键
     * @param value 要添加的元素
     * @return 操作后列表的长度
     */
    public static <T> long listAdd(@NotNull String key, @Nullable T value) {
        Long size = null;
        if (value != null) {
            size = redisTemplate.opsForList().rightPush(key, value);
        }
        return size == null ? 0 : size;
    }

    /**
     * 从列表右侧弹出元素并返回
     *
     * @param key  列表键
     * @param type 目标类型的 Class 对象
     * @return 包装了弹出元素的 Optional，否则为空
     */
    public static <T> Optional<T> listPop(@NotNull String key, @NotNull Class<T> type) {
        Object value = redisTemplate.opsForList().rightPop(key);
        return type.isInstance(value) ? Optional.of(type.cast(value)) : Optional.empty();
    }

    /**
     * 获取列表指定范围的元素
     *
     * @param key   列表键
     * @param start 起始索引
     * @param end   结束索引
     * @return 元素列表，无元素时返回空列表
     */
    public static @NotNull List<Object> listRange(@NotNull String key, long start, long end) {
        List<Object> range = redisTemplate.opsForList().range(key, start, end);
        return range == null ? List.of() : range;
    }

    /**
     * 获取列表长度
     *
     * @param key 列表键
     * @return 列表长度，键不存在时返回 0
     */
    public static long listSize(@NotNull String key) {
        Long size = redisTemplate.opsForList().size(key);
        return size == null ? 0 : size;
    }

    /**
     * 更新键值
     *
     * @param key   要更新的键
     * @param value 新值
     * @return 键存在且更新成功返回 true，否则返回 false
     */
    public static <T> boolean update(@NotNull String key, @Nullable T value) {
        if (value != null) {
            return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfPresent(key, value));
        }
        return false;
    }
}