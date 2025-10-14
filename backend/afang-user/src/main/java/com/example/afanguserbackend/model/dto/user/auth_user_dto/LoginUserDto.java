package com.example.afanguserbackend.model.dto.user.auth_user_dto;

import lombok.Data;

@Data
public class LoginUserDto {
    private String username;
    private String password;
    private String  phone;


}
