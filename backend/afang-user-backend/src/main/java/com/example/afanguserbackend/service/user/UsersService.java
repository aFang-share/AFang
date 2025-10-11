package com.example.afanguserbackend.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.afanguserbackend.model.dto.user.AddUsersDto;
import com.example.afanguserbackend.model.dto.user.LoginUserDto;
import com.example.afanguserbackend.model.dto.user.UpdateUsersDto;
import com.example.afanguserbackend.model.entity.user.Users;

import java.util.Map;

public interface
UsersService extends IService<Users> {
    /**
     * 添加用户
     *
     * @param addUsersDto
     */
    boolean addUsers(AddUsersDto addUsersDto);

    Map<String, String> loginUsers(LoginUserDto dto);

    boolean updateUsers(UpdateUsersDto dto);
}
