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
 * Redis工具类，提供Redis缓存操作的封装方法。
 * <p>
 * 该工具类封装了常用的Redis操作，包括：
 * <ul>
 *   <li>基本的键值对操作（存储、获取、删除）</li>
 *   <li>过期时间管理</li>
 *   <li>列表数据结构操作</li>
 *   <li>原子性自增操作</li>
 *   <li>批量操作</li>
 *   <li>模糊查询</li>
 * </ul>
 * <p>
 * 所有方法都经过空值检查和类型安全处理，使用Optional包装返回值以提高代码健壮性。
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@Component
public class RedisUtil {

    /**
     * Redis操作模板实例，通过Spring容器注入。
     */
    private static RedisTemplate<String, Object> redisTemplate;

    /**
     * Spring依赖注入构造器。
     * <p>
     * 初始化RedisTemplate实例，用于执行Redis操作。
     *
     * @param redisTemplate Spring容器中的RedisTemplate实例
     */
    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    /**
     * 存储键值对，无过期时间。
     * <p>
     * 如果值为null，则不执行存储操作。
     *
     * @param key   存储键，不能为null
     * @param value 存储值，可以为null
     * @param <T>   值的类型参数
     */
    public static <T> void set(@NotNull String key, @Nullable T value) {
        if (value != null) {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 存储键值对并设置过期时间。
     * <p>
     * 如果值为null，则不执行存储操作。
     *
     * @param key     存储键，不能为null
     * @param value   存储值，可以为null
     * @param timeout 过期时间
     * @param unit    时间单位，不能为null
     * @param <T>     值的类型参数
     */
    public static <T> void set(@NotNull String key, @Nullable T value, long timeout, @NotNull TimeUnit unit) {
        if (value != null) {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
        }
    }

    /**
     * 存储键值对并设置过期时间（使用Duration）。
     * <p>
     * 使用Duration对象指定过期时间，默认转换为秒数。
     *
     * @param key     存储键，不能为null
     * @param value   存储值，可以为null
     * @param timeout 过期时间，不能为null
     * @param <T>     值的类型参数
     */
    public static <T> void set(@NotNull String key, @Nullable T value, @NotNull Duration timeout) {
        set(key, value, timeout.toSeconds(), TimeUnit.SECONDS);
    }

    /**
     * 存储键值对并设置过期时间（默认单位为秒）。
     * <p>
     * 使用秒作为默认时间单位。
     *
     * @param key     存储键，不能为null
     * @param value   存储值，不能为null
     * @param timeout 过期时间（秒）
     * @param <T>     值的类型参数
     */
    public static <T> void set(@NotNull String key, @NotNull T value, long timeout) {
        set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 仅当键不存在时存储值（原子操作）。
     * <p>
     * 这是一个原子操作，只有在键不存在时才会设置值。
     * 如果值为null，则不执行存储操作并返回false。
     *
     * @param key   存储键，不能为null
     * @param value 存储值，可以为null
     * @param <T>   值的类型参数
     * @return 键不存在且存储成功返回true，否则返回false
     */
    public static <T> boolean setIfAbsent(@NotNull String key, @Nullable T value) {
        if (value != null) {
            return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value));
        }
        return false;
    }

    /**
     * 获取值并转换为指定类型。
     * <p>
     * 执行类型安全的值获取操作，如果值类型不匹配或键不存在，返回空Optional。
     *
     * @param key  存储键，不能为null
     * @param type 目标类型的Class对象，不能为null
     * @param <T>  目标类型参数
     * @return 包装了目标类型值的Optional，键不存在或类型不匹配时返回空Optional
     */
    public static <T> Optional<T> get(@NotNull String key, @NotNull Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        return type.isInstance(value) ? Optional.of(type.cast(value)) : Optional.empty();
    }

    /**
     * 快捷获取字符串值。
     * <p>
     * 这是获取字符串类型的便捷方法。
     *
     * @param key 存储键，不能为null
     * @return 包装了字符串值的Optional，键不存在时返回空Optional
     */
    public static Optional<String> getString(@NotNull String key) {
        return get(key, String.class);
    }

    /**
     * 批量获取多个键的值。
     * <p>
     * 一次性获取多个键对应的值，返回的列表与输入键集合的顺序对应。
     * 不存在的键对应的值为null。
     *
     * @param keys 键集合，不能为null
     * @return 与键顺序对应的值列表，不存在的键对应null值
     */
    public static List<Object> multiGet(@NotNull Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 删除指定键。
     * <p>
     * 删除单个键及其对应的值。
     *
     * @param key 要删除的键，不能为null
     * @return 删除成功返回true，键不存在返回false
     */
    public static boolean delete(@NotNull String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 批量删除键。
     * <p>
     * 一次性删除多个键，返回实际删除的键数量。
     *
     * @param keys 要删除的键集合，不能为null
     * @return 成功删除的键数量
     */
    public static long deleteBatch(@NotNull Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 检查键是否存在。
     *
     * @param key 要检查的键，不能为null
     * @return 存在返回true，否则返回false
     */
    public static boolean hasKey(@NotNull String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置键的过期时间。
     * <p>
     * 为已存在的键设置过期时间，如果键不存在则设置失败。
     *
     * @param key     要设置的键，不能为null
     * @param timeout 过期时间
     * @param unit    时间单位，不能为null
     * @return 设置成功返回true，键不存在返回false
     */
    public static boolean expire(@NotNull String key, long timeout, @NotNull TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置键的过期时间（使用Duration）。
     * <p>
     * 使用Duration对象指定过期时间，默认转换为秒数。
     *
     * @param key     要设置的键，不能为null
     * @param timeout 过期时间，不能为null
     * @return 设置成功返回true，键不存在返回false
     */
    public static boolean expire(@NotNull String key, @NotNull Duration timeout) {
        return expire(key, timeout.toSeconds(), TimeUnit.SECONDS);
    }

    /**
     * 移除键的过期时间（使其永久有效）。
     * <p>
     * 将键转换为永久有效，移除其过期时间设置。
     *
     * @param key 要操作的键，不能为null
     * @return 移除成功返回true，键不存在返回false
     */
    public static boolean persist(@NotNull String key) {
        return redisTemplate.persist(key);
    }

    /**
     * 获取键的剩余过期时间（秒）。
     * <p>
     * 返回键的剩余生存时间，单位为秒。
     *
     * @param key 要查询的键，不能为null
     * @return 剩余时间（秒），键不存在或已过期返回-2，永不过期返回-1
     */
    public static long getExpireSeconds(@NotNull String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 获取键的剩余过期时间（Duration）。
     * <p>
     * 使用Duration对象返回剩余过期时间。
     *
     * @param key 要查询的键，不能为null
     * @return 剩余时间Duration，键不存在或已过期返回Duration.ZERO
     */
    public static Duration getExpire(@NotNull String key) {
        return Duration.ofSeconds(getExpireSeconds(key));
    }

    /**
     * 模糊匹配查询键。
     * <p>
     * 支持通配符查询，如：user:*、*test等。
     * 注意：在生产环境中应谨慎使用，大量键的查询可能影响性能。
     *
     * @param pattern 匹配模式，支持通配符，不能为null
     * @return 匹配到的键集合，无匹配时返回空集合
     */
    public static @NotNull Set<String> keys(@NotNull String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 为键对应的值执行自增操作（支持整数）。
     * <p>
     * 这是一个原子操作，适用于计数器场景。如果键不存在，会先初始化为0再执行自增。
     *
     * @param key   要操作的键，不能为null
     * @param delta 增量，可以为负数实现自减
     * @return 自增后的结果值
     */
    public static long increment(@NotNull String key, long delta) {
        Long result = redisTemplate.opsForValue().increment(key, delta);
        return result == null ? 0 : result;
    }

    /**
     * 为键对应的值执行自增操作（支持浮点数）。
     * <p>
     * 这是一个原子操作，支持小数点精度。适用于需要精确计算的场景。
     *
     * @param key   要操作的键，不能为null
     * @param delta 增量，可以为负数实现自减
     * @return 自增后的结果值
     */
    public static double increment(@NotNull String key, double delta) {
        Double result = redisTemplate.opsForValue().increment(key, delta);
        return result == null ? 0 : result;
    }

    /**
     * 向列表左侧添加元素（头部插入）。
     * <p>
     * 将元素添加到列表的头部，如果值为null则不执行操作。
     *
     * @param key   列表键，不能为null
     * @param value 要添加的元素，可以为null
     * @param <T>   元素类型参数
     * @return 操作后列表的长度，值为null时返回0
     */
    public static <T> long listPush(@NotNull String key, @Nullable T value) {
        Long size = null;
        if (value != null) {
            size = redisTemplate.opsForList().leftPush(key, value);
        }
        return size == null ? 0 : size;
    }

    /**
     * 向列表右侧添加元素（尾部追加）。
     * <p>
     * 将元素添加到列表的尾部，如果值为null则不执行操作。
     *
     * @param key   列表键，不能为null
     * @param value 要添加的元素，可以为null
     * @param <T>   元素类型参数
     * @return 操作后列表的长度，值为null时返回0
     */
    public static <T> long listAdd(@NotNull String key, @Nullable T value) {
        Long size = null;
        if (value != null) {
            size = redisTemplate.opsForList().rightPush(key, value);
        }
        return size == null ? 0 : size;
    }

    /**
     * 从列表右侧弹出元素并返回（队列操作）。
     * <p>
     * 移除并返回列表最右侧的元素，实现了FIFO（先进先出）队列操作。
     * 如果列表为空或元素类型不匹配，返回空Optional。
     *
     * @param key  列表键，不能为null
     * @param type 目标类型的Class对象，不能为null
     * @param <T>  元素类型参数
     * @return 包装了弹出元素的Optional，列表为空或类型不匹配时返回空Optional
     */
    public static <T> Optional<T> listPop(@NotNull String key, @NotNull Class<T> type) {
        Object value = redisTemplate.opsForList().rightPop(key);
        return type.isInstance(value) ? Optional.of(type.cast(value)) : Optional.empty();
    }

    /**
     * 获取列表指定范围的元素。
     * <p>
     * 支持负数索引，-1表示最后一个元素。不会修改原列表。
     * 如果列表为空，返回空列表。
     *
     * @param key   列表键，不能为null
     * @param start 起始索引（包含）
     * @param end   结束索引（包含）
     * @return 元素列表，无元素时返回空列表
     */
    public static @NotNull List<Object> listRange(@NotNull String key, long start, long end) {
        List<Object> range = redisTemplate.opsForList().range(key, start, end);
        return range == null ? List.of() : range;
    }

    /**
     * 获取列表长度。
     * <p>
     * 返回列表中元素的数量，如果列表不存在返回0。
     *
     * @param key 列表键，不能为null
     * @return 列表长度，键不存在时返回0
     */
    public static long listSize(@NotNull String key) {
        Long size = redisTemplate.opsForList().size(key);
        return size == null ? 0 : size;
    }

    /**
     * 更新已存在的键值。
     * <p>
     * 只有在键存在时才会更新值，这是一个原子操作。
     * 如果值为null或键不存在，返回false。
     *
     * @param key   要更新的键，不能为null
     * @param value 新值，可以为null
     * @param <T>   值的类型参数
     * @return 键存在且更新成功返回true，否则返回false
     */
    public static <T> boolean update(@NotNull String key, @Nullable T value) {
        if (value != null) {
            return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfPresent(key, value));
        }
        return false;
    }
}