package com.example.afanguserbackend.model.dto.user.common_user_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/***
 * 用户更新信息
 * @param id 用户id
 * @param username 用户名
 * @param password 密码
 * @param email 邮箱
 * @param phone 手机号
 * @param avatar 头像
 * @param status 用户状态
 * @param userRole 用户角色
 */
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
