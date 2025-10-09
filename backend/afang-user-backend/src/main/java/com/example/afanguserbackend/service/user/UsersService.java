package com.example.afanguserbackend.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.afanguserbackend.common.PageRequest;
import com.example.afanguserbackend.common.PageResponse;
import com.example.afanguserbackend.model.dto.user.AddUsersDto;
import com.example.afanguserbackend.model.dto.user.LoginUserDto;
import com.example.afanguserbackend.model.entity.user.Users;
import com.example.afanguserbackend.model.vo.user.UserVo;

public interface
UsersService extends IService<Users> {
//添加用户
  UserVo addUser(AddUsersDto addUsersDto);
//  通过用户id查询
  UserVo getUserById(Long id);
  //  通过用户名查询
  UserVo getUserByUsername(String username);
  //  通过邮箱查询
  UserVo getUserByEmail(String email);
  //  更新用户信息
  UserVo updateUser(AddUsersDto addUsersDto);

  UserVo upDateUser(AddUsersDto addUsersDto);

  //  删除用户
  void deleteUser(Long id);
  //  分页查询用户
  PageResponse<UserVo> getUsers(PageRequest pageRequest);

  /**
   * 添加用户
   * @param addUsersDto
   */
  boolean addUsers(AddUsersDto addUsersDto);

  boolean loginUsers(AddUsersDto addUsersDto);


  boolean loginUsers(LoginUserDto dto);
}
