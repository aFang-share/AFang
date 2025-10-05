package com.example.afanguserbackend.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.afanguserbackend.model.dto.user.UsersDto;
import com.example.afanguserbackend.model.entity.user.Users;
import com.example.afanguserbackend.model.vo.user.UserVo;

public interface UsersService extends IService<Users> {
  UserVo addUser(UsersDto usersDto);


}
