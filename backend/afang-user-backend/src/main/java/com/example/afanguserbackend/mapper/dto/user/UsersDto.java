package com.example.afanguserbackend.mapper.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsersDto {

    private String username;
    private String password;
    private String email;

}
