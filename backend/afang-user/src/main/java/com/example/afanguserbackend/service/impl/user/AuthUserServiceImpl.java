package com.example.afanguserbackend.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.afanguserbackend.mapper.user.UsersMapper;
import com.example.afanguserbackend.model.dto.user.auth_user_dto.LoginUserDto;
import com.example.afanguserbackend.model.dto.user.auth_user_dto.RegisterUsersDto;
import com.example.afanguserbackend.model.entity.user.Users;
import com.example.afanguserbackend.service.user.AuthUserService;
import com.example.afanguserbackend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthUserServiceImpl extends ServiceImpl<UsersMapper, Users> implements AuthUserService {
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public boolean addUsers(RegisterUsersDto dto) {
//        TODO：代码优化
        log.info("接收信息", dto);
        Users users = new Users();
        Users user = baseMapper.selectOne(new QueryWrapper<Users>().eq("username", dto.getUsername()));
        if (user == null) {//            用户名不存在
            BeanUtils.copyProperties(dto, users);
            users.setPassword(passwordEncoder.encode(dto.getPassword()));
            return this.save(users);
        } else {
            return false;
        }
    }

    @Override
    public Map<String, String> loginUsers(LoginUserDto dto) {
        Users user = baseMapper.selectOne(new QueryWrapper<Users>().eq("username", dto.getUsername()));
        if (user == null) {
            throw new RuntimeException("用户不存在");
        } else {
            //生成token
            String token = jwtUtil.generateToken(user);
//用户存在验证密码
            if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                throw new RuntimeException("用户不存在");
            }
            Map<String, String> LoginMap = new HashMap<>();
            LoginMap.put("token", token);
            return LoginMap;
        }
    }
}
