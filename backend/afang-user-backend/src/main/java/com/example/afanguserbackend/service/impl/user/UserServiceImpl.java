package com.example.afanguserbackend.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.afanguserbackend.mapper.user.UsersMapper;
import com.example.afanguserbackend.model.dto.user.AddUsersDto;
import com.example.afanguserbackend.model.dto.user.LoginUserDto;
import com.example.afanguserbackend.model.dto.user.UpdateUsersDto;
import com.example.afanguserbackend.model.entity.user.Users;
import com.example.afanguserbackend.service.user.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//业务实现
//UserVo 应该只包含用户ID、用户名、邮箱、创建时间等非敏感信息，密码字段应该完全不存在于VO中。

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean addUsers(AddUsersDto dto){
//        TODO：代码优化
        Users users = new Users();
        Users user = baseMapper.selectOne(new QueryWrapper<Users>().eq("username", dto.getUsername()));
        if (user==null){//            用户名不存在
            BeanUtils.copyProperties(dto, users);
            users.setPassword(passwordEncoder.encode(dto.getPassword()));
            return this.save(users);
        }else {
            return false;
            }
    }


    @Override
    public boolean loginUsers(LoginUserDto dto){
        Users users = new Users();
        Users user = baseMapper.selectOne(new QueryWrapper<Users>().eq("username", dto.getUsername()));
        if (user==null){
            return false;

        }else {
//用户存在验证密码
            return passwordEncoder.matches(dto.getPassword(),user.getPassword());
        }
    }

    @Override
    public boolean updateUsers(UpdateUsersDto dto) {
        Users users = new Users();
        Users user = baseMapper.selectOne(new QueryWrapper<Users>().eq("id", dto.getId()));
        if (user==null){
            return false;
        }else {
//            更改user info
            BeanUtils.copyProperties(dto, users);
            return this.updateById(users);
        }
    }
}
