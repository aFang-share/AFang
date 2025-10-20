package com.example.afanguserbackend.model.dto.user.auth_user_dto;

import lombok.Data;

/**
 * 用户登录数据传输对象
 * 用于接收用户登录时提交的认证信息
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@Data
public class LoginUserDto {
    /**
     * 用户名
     * 用户登录时使用的唯一标识符
     */
    private String username;

    /**
     * 密码
     * 用户登录密码，需要进行加密验证
     */
    private String password;

    /**
     * 手机号
     * 用户绑定的手机号码，可用于手机号登录或验证
     */
    private String phone;
}
