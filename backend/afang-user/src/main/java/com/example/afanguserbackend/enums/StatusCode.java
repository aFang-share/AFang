package com.example.afanguserbackend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统状态码枚举
 * 定义应用程序中使用的各种状态码和对应的消息
 * 提供统一的响应状态管理
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@Getter
@AllArgsConstructor
public enum StatusCode {

    /**
     * 成功状态
     * 表示请求处理成功
     */
    SUCCESS("20000", "success"),

    /**
     * 系统错误状态
     * 表示系统内部错误，如数据库连接失败、服务异常等
     */
    SYSTEM_ERROR("50000", "system error"),

    /**
     * 运行时异常状态
     * 表示业务逻辑处理过程中的异常，需要用户稍后重试
     */
    RUN_ERROR("99999", "请稍后再试!");

    /**
     * 状态码
     * 用于标识不同的响应状态
     */
    private final String code;

    /**
     * 状态消息
     * 用于向用户展示的状态描述信息
     */
    private final String message;


    // 如果需要其他状态码，可以继续添加
    // 例如：PARAM_ERROR("40001", "参数错误")
    //       AUTH_ERROR("40101", "认证失败")
    //       FORBIDDEN_ERROR("40301", "权限不足")
}
