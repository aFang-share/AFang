package com.example.afanguserbackend.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.afanguserbackend.model.dto.user.AddUsersDto;
import com.example.afanguserbackend.model.dto.user.LoginUserDto;
import com.example.afanguserbackend.model.dto.user.UpdateUsersDto;
import com.example.afanguserbackend.model.entity.user.Users;

public interface
UsersService extends IService<Users> {
 /**
   * 添加用户
   * @param addUsersDto
   */
  boolean addUsers(AddUsersDto addUsersDto);
  boolean loginUsers(LoginUserDto dto);
  boolean updateUsers(UpdateUsersDto dto);
}
