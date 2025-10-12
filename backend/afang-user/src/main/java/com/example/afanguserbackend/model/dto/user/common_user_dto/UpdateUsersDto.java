package com.example.afanguserbackend.model.dto.user.common_user_dto;

import lombok.Data;

@Data
public class UpdateUsersDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String avatar;
    private String status;
    private String userRole;
}
