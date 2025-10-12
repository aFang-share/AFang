package com.example.afanguserbackend.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.afanguserbackend.model.dto.user.common_user_dto.UpdateUsersDto;
import com.example.afanguserbackend.model.entity.user.Users;

public interface CommonUserService extends IService<Users> {
    // UsersService 处理用户信息管理
    //  - 查询用户信息
    //  - 更新用户资料
    boolean updateUsers(UpdateUsersDto dto);

}
