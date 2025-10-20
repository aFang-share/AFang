package com.example.afanguserbackend.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.afanguserbackend.mapper.user.UsersMapper;
import com.example.afanguserbackend.model.dto.user.auth_user_dto.LoginUserDto;
import com.example.afanguserbackend.model.dto.user.auth_user_dto.RegisterUsersDto;
import com.example.afanguserbackend.model.entity.user.Users;
import com.example.afanguserbackend.service.user.AuthUserService;
import com.example.afanguserbackend.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * 用户认证服务实现类
 * 实现用户注册、登录、验证码发送等认证相关功能
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthUserServiceImpl extends ServiceImpl<UsersMapper, Users> implements AuthUserService {

    /**
     * 密码编码器
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * JWT工具类
     */
    private final JwtUtil jwtUtil;

    /**
     * Redis工具类
     */
    private final RedisUtil redisUtil;

    /**
     * 邮件工具类
     */
    private final EmailUtil emailUtil;

    /**
     * 用户缓存Key前缀
     */
    private static final String USER_CACHE_KEY_PREFIX = "users:user:";

    /**
     * 加密Key前缀
     */
    private static final String KEY = "keys:key:";

    /**
     * 验证码缓存Key前缀
     */
    private static final String VERIFICATION_CODE_KEY_PREFIX = "auth:verification-code:";

    /**
     * 用户缓存时间：24小时
     */
    private static final Duration USER_CACHE_DURATION = Duration.ofMinutes(24);

    /**
     * 用户注册实现
     * 验证用户信息，创建账户并生成Token
     *
     * @param dto 用户注册信息
     * @return 包含JWT Token的Map
     * @throws Exception 注册过程中的异常
     */
    @Override
    public Map<String, String> registerUsers(RegisterUsersDto dto) throws Exception {
        log.info("开始处理用户注册，注册信息：{}", dto);

        // 检查用户是否已存在
        Users existingUser = baseMapper.selectOne(new QueryWrapper<Users>().eq("phone", dto.getPhone()));
        if (existingUser != null) {
            throw new RuntimeException("用户已存在");
        }

        // 创建新用户对象
        Users newUser = new Users();
        BeanUtils.copyProperties(dto, newUser);

        // 加密密码
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));

        // 验证邮箱验证码
        if (!CommonUtil.validateVerificationCode(dto.getCode(), dto.getEmail())) {
            throw new RuntimeException("验证码错误");
        }

        // 保存用户到数据库
        boolean saved = this.save(newUser);
        if (!saved) {
            throw new RuntimeException("用户创建失败!");
        }

        // 将用户信息缓存到Redis
        // TODO: 优化缓存Key，考虑使用UUID作为用户唯一标识
        RedisUtil.set(USER_CACHE_KEY_PREFIX + newUser.getPhone(), newUser, USER_CACHE_DURATION);

        // TODO: 删除已使用的邮箱验证码

        // 生成并返回JWT Token
        Map<String, String> result = new HashMap<>();
        result.put("token", jwtUtil.generateToken(newUser));

        log.info("用户注册成功，用户ID：{}", newUser.getId());
        return result;
    }

    /**
     * 用户登录实现
     * 验证用户凭据，从缓存或数据库获取用户信息并生成Token
     *
     * @param dto 用户登录信息
     * @return 包含JWT Token的Map
     */
    @Override
    public Map<String, String> loginUsers(LoginUserDto dto) {
        // 从缓存中获取用户信息
        Optional<Users> userOptional = RedisUtil.get(USER_CACHE_KEY_PREFIX + dto.getPhone(), Users.class);

        Users user = userOptional
                .orElseGet(() -> {
                    // 缓存中不存在，从数据库查询
                    Users dbUser = baseMapper.selectOne(new QueryWrapper<Users>().eq("phone", dto.getPhone()));
                    if (dbUser == null) {
                        throw new RuntimeException("用户不存在");
                    }

                    // 验证密码
                    if (!passwordEncoder.matches(dto.getPassword(), dbUser.getPassword())) {
                        throw new RuntimeException("用户名或密码错误");
                    }
                    return dbUser;
                });

        log.info("用户登录成功，用户信息：{}", user);

        // 更新用户缓存
        RedisUtil.set(USER_CACHE_KEY_PREFIX + dto.getPhone(), user, USER_CACHE_DURATION);

        // 生成并返回JWT Token
        Map<String, String> result = new HashMap<>();
        result.put("token", jwtUtil.generateToken(user));

        return result;
    }

    /**
     * 发送邮箱验证码实现
     * 调用邮件工具类发送验证码邮件
     *
     * @param email 接收验证码的邮箱地址
     * @throws Exception 发送过程中的异常
     */
    @Override
    public void sendCodeByEmail(String email) throws Exception {
        emailUtil.sendEmailByCode(email);
    }

    /**
     * 验证邮箱验证码实现
     * 验证邮箱验证码的有效性
     *
     * @param code 待验证的验证码
     * @param email 验证码对应的邮箱地址
     * @return 验证结果，true表示验证成功
     */
    @Override
    public boolean validateEmailCode(String code, String email) {
        return CommonUtil.validateVerificationCode(code, email);
    }

    /**
     * 发送手机验证码实现
     * 生成验证码，缓存到Redis，并发送短信
     *
     * @param phone 接收验证码的手机号
     * @return 发送结果响应
     * @throws Exception 发送过程中的异常
     */
    @Override
    public void sendCodeByPhone(String phone) throws Exception {
        // 生成6位验证码
        String verificationCode = CommonUtil.getVerificationCode();

        // 将验证码缓存到Redis，有效期5分钟
        RedisUtil.set(VERIFICATION_CODE_KEY_PREFIX + phone, verificationCode, 5, TimeUnit.MINUTES);

        // 发送短信验证码
        PhoneUtil.sendPhoneCode(phone, verificationCode, "5");

          }
}





