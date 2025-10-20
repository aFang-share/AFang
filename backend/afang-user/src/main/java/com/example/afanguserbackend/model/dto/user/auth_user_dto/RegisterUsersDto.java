package com.example.afanguserbackend.model.dto.user.auth_user_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册数据传输对象
 * 用于接收用户注册时提交的信息，包含必要的验证字段
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUsersDto {
    /**
     * 用户名
     * 新用户注册时使用的唯一标识符
     */
    private String username;

    /**
     * 密码
     * 用户登录密码，不能为空
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 验证码
     * 邮箱验证码，用于验证邮箱所有权，不能为空
     */
    @NotBlank(message = "验证码不能为空")
    private String code;

    /**
     * 邮箱
     * 用户注册邮箱地址，用于接收验证码和后续通知，不能为空且格式必须正确
     */
    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "邮箱格式不正确")
    private String email;

    /**
     * 手机号
     * 用户绑定的手机号码，可选字段
     */
    private String phone;
}
