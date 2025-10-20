package com.example.afanguserbackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应封装类
 * 用于封装所有API的响应结果，提供统一的数据格式
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 * @param <T> 响应数据的泛型类型
 */
@Data
public class BaseResponse<T> implements Serializable {

    /**
     * 状态码
     * 用于表示请求处理结果的状态
     */
    private String code;

    /**
     * 响应数据
     * 泛型数据字段，可以包含任何类型的响应数据
     */
    private T data;

    /**
     * 响应信息
     * 描述性信息，用于给前端展示提示信息
     */
    private String message;

    /**
     * 构造函数
     *
     * @param code 状态码，通常表示请求成功或失败的状态
     * @param message 描述性信息，用于给前端展示提示信息
     * @param data 泛型数据字段，可以包含任何类型的响应数据
     */
    public BaseResponse(String code, String message, T data) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    // 注意：Map类型构造函数方法未实现，建议使用ResultUtils工具类创建响应
}
