package com.example.afanguserbackend.model.dto.user.auth_user_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank(message = "密码不能为空")
    private String password;
        @NotBlank(message = "验证码不能为空")
    private String code;
    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "邮箱格式不正确")
    private String email;
    private String phone;
}
