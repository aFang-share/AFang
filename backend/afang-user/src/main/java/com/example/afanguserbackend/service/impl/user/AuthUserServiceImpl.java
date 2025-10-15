package com.example.afanguserbackend.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.afanguserbackend.mapper.user.UsersMapper;
import com.example.afanguserbackend.model.dto.user.auth_user_dto.LoginUserDto;
import com.example.afanguserbackend.model.dto.user.auth_user_dto.RegisterUsersDto;
import com.example.afanguserbackend.model.entity.user.Users;
import com.example.afanguserbackend.service.user.AuthUserService;
import com.example.afanguserbackend.utils.JwtUtil;
import com.example.afanguserbackend.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthUserServiceImpl extends ServiceImpl<UsersMapper, Users> implements AuthUserService {
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private static final String USER_CACHE_KEY_PREFIX = "users:user:";
    private static final String KEY = "keys:key:";
    private static final String VERIFICATION_CODE_KEY_PREFIX = "auth:verification-code:";
    private static final Duration USER_CACHE_DURATION = Duration.ofMinutes(24);
    private final RedisUtil redisUtil;

    @Override
    public String registerUsers(RegisterUsersDto dto) {
//        TODO：代码优化
        log.info("接收信息", dto);
        Users users = new Users();
        Users user = baseMapper.selectOne(new QueryWrapper<Users>().eq("phone", dto.getPhone()));
        if (user == null) {//            用户不存在
            BeanUtils.copyProperties(dto, users);
            users.setPassword(passwordEncoder.encode(dto.getPassword()));
            boolean saved = this.save(users);
            if (!saved) {
                throw new RuntimeException("用户创建失败!");
            }

            //将用户信息加入缓存
//TODO：edisUtil.set这里设置的Key需要优化为UUID。一个用户的标识。
            redisUtil.set(USER_CACHE_KEY_PREFIX + users.getPhone(), users, USER_CACHE_DURATION);
//            TODO:email验证码删除
//            redisUtil.delete(VERIFICATION_CODE_KEY_PREFIX + RegisterUsersDto.getEmail());

            //生成token并且返回
            return jwtUtil.generateToken(users);
        }
//        如果用户存在直接抛出错误
        throw new RuntimeException("用户已存在");
    }

    @Override
    public Map<String, String> loginUsers(LoginUserDto dto) {
//         redisUtil.get() 方法返回的是 Optional<Object>，
        Optional<Object> userOptional = redisUtil.get(USER_CACHE_KEY_PREFIX + dto.getPhone());
        Users userRedis = (Users) userOptional
                .orElseGet(() -> {
                    Users userDB = baseMapper.selectOne(new QueryWrapper<Users>().eq("phone", dto.getPhone()));
                    if (userDB == null) {
                        throw new RuntimeException("用户不存在");
                    }
//解密
                    if (!passwordEncoder.matches(dto.getPassword(), userDB.getPassword())) {
                        throw new RuntimeException("用户不存在");
                    }
                    return userDB;//不是吧
                });
        log.info("————————————————————————————————{}", userRedis);
        if (userRedis == null) {
            throw new RuntimeException("用户不存在");
        }
        redisUtil.set(USER_CACHE_KEY_PREFIX + dto.getPhone(), userRedis, USER_CACHE_DURATION);
        Map<String, String> LoginMap = new HashMap<>();
        LoginMap.put("token", jwtUtil.generateToken(userRedis));
        return LoginMap;
//            会不会是和上面一样编辑器
    }
}

