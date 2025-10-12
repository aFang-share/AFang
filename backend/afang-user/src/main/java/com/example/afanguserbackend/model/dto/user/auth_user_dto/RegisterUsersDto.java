package com.example.afanguserbackend.model.dto.user.auth_user_dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUsersDto {
    private Long id;

    private String username;
    private String password;
    private String email;
    private String phone;
    private String avatar;
    private String status;
    private String userRole;
    private Integer pageNum = 1;
    private Integer pageSize = 10;

}
