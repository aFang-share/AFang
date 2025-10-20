package com.example.afanguserbackend.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 通过响应类
 *
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {
    //状态码

    private String code;
    //响应数据
    private T data;
//响应信息
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

    /**
     * Map类型构造函数
     * @return
     */
//    TODO: Map类型构造函数目前未实现 return null;

    public static Map<String, String> success() {
        return null;
    }
}
