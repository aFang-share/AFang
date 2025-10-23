package com.example.afanguserbackend.exception;

import com.example.afanguserbackend.common.BaseResponse;
import com.example.afanguserbackend.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局异常处理器
 * 统一处理应用程序中抛出的各种异常，返回标准化的错误响应
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理运行时异常
     * 捕获所有RuntimeException及其子类异常
     *
     * @param e 运行时异常对象
     * @return 包含错误信息的响应对象
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<String> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常：{}", e.getMessage(), e);
        return ResultUtils.fail(e.getMessage());
    }

    /**
     * 处理通用异常
     * 捕获所有Exception及其子类异常，作为兜底处理
     *
     * @param e 异常对象
     * @return 包含错误信息的响应对象
     */
    @ExceptionHandler(Exception.class)
    public BaseResponse<String> handleException(Exception e) {
        log.error("系统异常：{}", e.getMessage(), e);
        return ResultUtils.fail(e.getMessage());
    }
}
