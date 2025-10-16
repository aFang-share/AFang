package com.example.afanguserbackend.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.afanguserbackend.mapper.user.UsersMapper;
import com.example.afanguserbackend.model.dto.user.auth_user_dto.LoginUserDto;
import com.example.afanguserbackend.model.dto.user.auth_user_dto.RegisterUsersDto;
import com.example.afanguserbackend.model.entity.user.Users;
import com.example.afanguserbackend.service.user.AuthUserService;
import com.example.afanguserbackend.utils.EmailUtil;
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
    private final EmailUtil emailUtil;

    @Override
    public Map<String, String>  registerUsers(RegisterUsersDto dto) throws Exception {
        //        TODO：代码优化
        //写日志遵循lombok规范
        log.info("接收信息{}", dto);
        Users users = new Users();
        Users user = baseMapper.selectOne(new QueryWrapper<Users>().eq("phone", dto.getPhone()));
        if (user == null) {//            用户不存在
            BeanUtils.copyProperties(dto, users);
            users.setPassword(passwordEncoder.encode(dto.getPassword()));
            //比对注册时的接收验证码
            if (!emailUtil.validateEmailCode(dto.getCode(), dto.getEmail())) {
                throw new RuntimeException("验证码错误");
            }
            boolean saved = this.save(users);
            if (!saved) {
                throw new RuntimeException("用户创建失败!");
            }

            //将用户信息加入缓存
            //TODO：edisUtil.set这里设置的Key需要优化为UUID。一个用户的标识。
            RedisUtil.set(USER_CACHE_KEY_PREFIX + users.getPhone(), users, USER_CACHE_DURATION);
            //            TODO:email验证码删除
            //生成token并且返回
            Map<String, String> registerMap = new HashMap<>();
            registerMap.put("token", jwtUtil.generateToken(users));
            return registerMap;
        }
            //        如果用户存在直接抛出错误

        throw new RuntimeException("用户已存在");
    }

    @Override
    public Map<String, String> loginUsers(LoginUserDto dto) {
//         redisUtil.get() 方法返回的是 Optional<Object>，
        Optional<Users> userOptional = RedisUtil.get(USER_CACHE_KEY_PREFIX + dto.getPhone(), Users.class);
        Users userRedis = userOptional
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
        RedisUtil.set(USER_CACHE_KEY_PREFIX + dto.getPhone(), userRedis, USER_CACHE_DURATION);
        Map<String, String> LoginMap = new HashMap<>();
        LoginMap.put("token", jwtUtil.generateToken(userRedis));
        return LoginMap;
    //            会不会是和上面一样编辑器
    }

    @Override
    public void sendCodeByEmail(String email) throws Exception{
        emailUtil.sendEmailByCode(email);
        }

    /**
     * 邮箱验证码验证
     *
     */
    @Override
    public boolean validateEmailCode(String code, String email) {
        return emailUtil.validateEmailCode(code, email);
    }
    }





