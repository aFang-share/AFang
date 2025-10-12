package com.example.afanguserbackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通过响应类
 *
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {

    private String code;

    private T data;

    private String message;

    public BaseResponse(String code, String message, T data) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

}
