package com.example.afanguserbackend.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.afanguserbackend.model.dto.user.UsersDto;
import com.example.afanguserbackend.model.entity.user.Users;
import com.example.afanguserbackend.model.vo.user.UserVo;

public interface UsersService extends IService<Users> {
//添加用户
  UserVo addUser(UsersDto usersDto);
//  通过用户id查询
  UserVo getUserById(Long id);
  //  通过用户名查询
  UserVo getUserByUsername(String username);
  //  通过邮箱查询
  UserVo getUserByEmail(String email);
  //  更新用户信息
  UserVo updateUser(UsersDto usersDto);
  //  删除用户
  void deleteUser(Long id);
  //  分页查询用户
//  PageResponse<UserVo> getUsers(PageRequest pageRequest);
}
