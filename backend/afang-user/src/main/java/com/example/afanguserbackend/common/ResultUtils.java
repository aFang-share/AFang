package com.example.afanguserbackend.common;

import com.example.afanguserbackend.enums.StatusCode;

/**
 * 快速构造响应结果。的工具类
 */
public class ResultUtils {

    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage(), null);
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage(), data);
    }

    public static <T> BaseResponse<T> fail() {
        return new BaseResponse<>(StatusCode.SYSTEM_ERROR.getCode(), StatusCode.SYSTEM_ERROR.getMessage(), null);
    }

    public static <T> BaseResponse<T> fail(T data) {
        return new BaseResponse<>(StatusCode.SYSTEM_ERROR.getCode(), StatusCode.SYSTEM_ERROR.getMessage(), data);
    }

    //错误码细分
    public static <T> BaseResponse<T> fail(StatusCode statusCode) {
        return new BaseResponse<>(statusCode.getCode(), statusCode.getMessage(), null);
    }

    public static <T> BaseResponse<T> fail(String code, String message) {
        return new BaseResponse<>(code, message, null);
    }

    public static <T> BaseResponse<T> fail(String message) {
        return new BaseResponse<>(StatusCode.RUN_ERROR.getCode(), message, null);
    }
}