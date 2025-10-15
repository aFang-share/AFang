package com.example.afanguserbackend.model.dto.user.auth_user_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
//使用Lombook 注解，添加无参构造函数。
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUsersDto {
    private String username;
    private String password;
    private String phone;
}
