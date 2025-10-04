package com.example.afanguserbackend.service.impl.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.afanguserbackend.mapper.user.UsersMapper;
import com.example.afanguserbackend.mapper.dto.user.UsersDto;
import com.example.afanguserbackend.model.entity.user.Users;
import com.example.afanguserbackend.model.vo.user.UserVo;
import com.example.afanguserbackend.service.user.UsersService;
import org.springframework.stereotype.Service;

//业务实现

@Service
public class UserServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Override
    public UserVo addUser(UsersDto usersDto) {

        baseMapper.insert(Users.builder()
                .username(usersDto.getUsername())
                .password(usersDto.getPassword())
                .email(usersDto.getEmail())
                .build());
        UserVo userVo =new  UserVo();
        userVo.setUsername(usersDto.getUsername());
        userVo.setPassword(usersDto.getPassword());
        return userVo;
    }
}
