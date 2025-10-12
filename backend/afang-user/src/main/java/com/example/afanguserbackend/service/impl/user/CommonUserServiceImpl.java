package com.example.afanguserbackend.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.afanguserbackend.mapper.user.UsersMapper;
import com.example.afanguserbackend.model.dto.user.common_user_dto.UpdateUsersDto;
import com.example.afanguserbackend.model.entity.user.Users;
import com.example.afanguserbackend.model.wrapper.SecurityUserWrapper;
import com.example.afanguserbackend.service.user.CommonUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommonUserServiceImpl extends ServiceImpl<UsersMapper, Users> implements CommonUserService, UserDetailsService {
    @Override
    public boolean updateUsers(UpdateUsersDto dto) {
        Users users = new Users();
        Users user = baseMapper.selectOne(new QueryWrapper<Users>().eq("id", dto.getId()));
        if (user == null) {
            return false;
        } else {
//            更改user info
            BeanUtils.copyProperties(dto, users);
            return this.updateById(users);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        Users user = baseMapper.selectOne(this.lambdaQuery().eq(Users::getPhone, phone));
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        return new SecurityUserWrapper(user);
    }
}
