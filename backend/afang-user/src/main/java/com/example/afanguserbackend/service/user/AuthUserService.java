package com.example.afanguserbackend.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.afanguserbackend.model.dto.user.auth_user_dto.LoginUserDto;
import com.example.afanguserbackend.model.dto.user.auth_user_dto.RegisterUsersDto;
import com.example.afanguserbackend.model.entity.user.Users;

import java.util.Map;

public interface AuthUserService extends IService<Users> {
    // AuthService 处理用户认证、密码相关
//  - 注册时创建 Users
//  - 登录时查询 Users
//  - 密码修改时更新 Users
    /**
     * 添加用户
     *
     * @param registerUsersDto
     */
    Map<String, String> registerUsers(RegisterUsersDto registerUsersDto) throws Exception;

    Map<String, String> loginUsers(LoginUserDto dto);
    void sendCodeByEmail(String email)throws Exception;
    boolean validateEmailCode(String code, String email);
    Map<String,String>sendCodeByPhone(String phone) throws Exception;
}
