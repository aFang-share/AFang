package com.example.afanguserbackend.model.dto.user.auth_user_dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUsersDto {
    private String username;
    private String password;
    private String phone;
}
