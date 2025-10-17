package com.example.afanguserbackend.utils;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class CommonUtil {
    /**
     * 6位数字验证码生成
     */
    public static String getVerificationCode() {
        // 验证码生成逻辑
        // 生成一个6位随机数字验证码,手机验证码不支持字母和其他图形
        return Integer.toString(ThreadLocalRandom.current().nextInt(900000) + 100000);
    }
    /**
     * 验证码验证
     */
    public static boolean validateVerificationCode(String code, String address) {
        // 验证码验证逻辑
        //1、查redis
        Optional<String> redisCode= RedisUtil.getString(address);
        //2、验证code
        if(redisCode.isPresent() && redisCode.get().equals(code)){
            //3、验证通过删除redis
            return RedisUtil.delete(address);
        }
        return false;
    }
}
