package com.example.afanguserbackend.common;

import com.example.afanguserbackend.enums.StatusCode;

/**
 * 响应结果工具类
 * 提供快速构造统一响应结果的静态方法，简化Controller层的响应处理
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
public class ResultUtils {

    /**
     * 构造成功响应（无数据）
     * 使用默认成功状态码和消息
     *
     * @param <T> 响应数据泛型类型
     * @return 成功响应对象，data为null
     */
    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage(), null);
    }

    /**
     * 构造成功响应（带数据）
     * 使用默认成功状态码和消息，包含响应数据
     *
     * @param data 响应数据
     * @param <T> 响应数据泛型类型
     * @return 成功响应对象，包含指定数据
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage(), data);
    }

    /**
     * 构造失败响应（默认系统错误）
     * 使用默认系统错误状态码和消息
     *
     * @param <T> 响应数据泛型类型
     * @return 失败响应对象，data为null
     */
    public static <T> BaseResponse<T> fail() {
        return new BaseResponse<>(StatusCode.SYSTEM_ERROR.getCode(), StatusCode.SYSTEM_ERROR.getMessage(), null);
    }

    /**
     * 构造失败响应（带数据）
     * 使用默认系统错误状态码和消息，包含响应数据
     *
     * @param data 响应数据
     * @param <T> 响应数据泛型类型
     * @return 失败响应对象，包含指定数据
     */
    public static <T> BaseResponse<T> fail(T data) {
        return new BaseResponse<>(StatusCode.SYSTEM_ERROR.getCode(), StatusCode.SYSTEM_ERROR.getMessage(), data);
    }

    /**
     * 构造失败响应（指定状态码枚举）
     * 使用指定的状态码枚举，支持错误码细分
     *
     * @param statusCode 状态码枚举
     * @param <T> 响应数据泛型类型
     * @return 失败响应对象，使用指定状态码
     */
    public static <T> BaseResponse<T> fail(StatusCode statusCode) {
        return new BaseResponse<>(statusCode.getCode(), statusCode.getMessage(), null);
    }

    /**
     * 构造失败响应（自定义状态码和消息）
     * 允许完全自定义状态码和错误消息
     *
     * @param code 状态码
     * @param message 错误消息
     * @param <T> 响应数据泛型类型
     * @return 失败响应对象，使用自定义状态码和消息
     */
    public static <T> BaseResponse<T> fail(String code, String message) {
        return new BaseResponse<>(code, message, null);
    }

    /**
     * 构造失败响应（自定义消息）
     * 使用运行时错误状态码，自定义错误消息
     *
     * @param message 错误消息
     * @param <T> 响应数据泛型类型
     * @return 失败响应对象，使用自定义消息
     */
    public static <T> BaseResponse<T> fail(String message) {
        return new BaseResponse<>(StatusCode.RUN_ERROR.getCode(), message, null);
    }
}