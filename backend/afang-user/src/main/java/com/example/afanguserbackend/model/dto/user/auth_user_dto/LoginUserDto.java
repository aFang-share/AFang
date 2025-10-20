package com.example.afanguserbackend.model.dto.user.auth_user_dto;

import lombok.Data;

@Data
/**
 * 登录用户信息
 * @param username 用户名
 *@param password 密码
 * @param phone 手机号
 */
public class LoginUserDto {
    private String username;
    private String password;
    private String  phone;
}
