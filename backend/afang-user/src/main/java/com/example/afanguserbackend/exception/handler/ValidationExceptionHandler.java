package com.example.afanguserbackend.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 参数验证异常处理器
 * 专门处理请求参数验证失败时抛出的MethodArgumentNotValidException
 * 返回详细的字段验证错误信息，帮助前端精确定位问题
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@ControllerAdvice
public class ValidationExceptionHandler {

    /**
     * 处理参数验证异常
     * 当请求体中的参数不符合@Valid注解验证规则时触发
     *
     * @param ex 参数验证异常对象，包含所有验证失败的字段信息
     * @return 包含字段名和错误消息映射的响应实体，HTTP状态码400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // 遍历所有字段验证错误，构建字段名到错误消息的映射
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
