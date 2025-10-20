package com.example.afanguserbackend.utils;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 通用工具类。
 * <p>
 * 该工具类提供常用的验证码生成和验证功能，包括：
 * <ul>
 *   <li>6位数字验证码生成</li>
 *   <li>基于Redis缓存的验证码验证</li>
 *   <li>验证码的原子性删除（一次性使用）</li>
 * </ul>
 * <p>
 * 验证码验证采用一次性使用机制，验证成功后自动删除缓存中的验证码，
 * 确保验证码只能使用一次，提高安全性。
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
public class CommonUtil {

    /**
     * 生成6位数字验证码。
     * <p>
     * 生成一个6位随机数字验证码，范围在100000-999999之间。
     * 使用ThreadLocalRandom确保线程安全和更好的随机性。
     * 注意：手机短信验证码通常只支持数字，不支持字母和特殊字符。
     *
     * @return 6位数字验证码字符串
     */
    public static String getVerificationCode() {
        // 生成一个6位随机数字验证码，确保首位不为0
        return Integer.toString(ThreadLocalRandom.current().nextInt(900000) + 100000);
    }

    /**
     * 验证验证码的有效性。
     * <p>
     * 从Redis缓存中获取存储的验证码并与用户输入的验证码进行比较。
     * 验证过程：
     * <ol>
     *   <li>根据地址（手机号或邮箱）从Redis中获取缓存的验证码</li>
     *   <li>比较用户输入的验证码与缓存中的验证码</li>
     *   <li>如果验证成功，删除缓存中的验证码（确保一次性使用）</li>
     * </ol>
     *
     * @param code   用户输入的验证码
     * @param address 用户地址（手机号或邮箱地址，作为Redis的key）
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean validateVerificationCode(String code, String address) {
        // 从Redis中获取缓存的验证码
        Optional<String> redisCode = RedisUtil.getString(address);

        // 验证码匹配检查
        if (redisCode.isPresent() && redisCode.get().equals(code)) {
            // 验证通过后立即删除Redis中的验证码，确保一次性使用
            return RedisUtil.delete(address);
        }

        // 验证失败（验证码不存在、不匹配或已过期）
        return false;
    }
}
