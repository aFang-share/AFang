package com.example.afanguserbackend.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.afanguserbackend.model.dto.user.auth_user_dto.LoginUserDto;
import com.example.afanguserbackend.model.dto.user.auth_user_dto.RegisterUsersDto;
import com.example.afanguserbackend.model.entity.user.Users;

import java.util.Map;

/**
 * 用户认证服务接口
 * 提供用户注册、登录、验证码发送等认证相关功能
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
public interface AuthUserService extends IService<Users> {

    /**
     * 用户注册方法
     * 验证用户注册信息，创建新用户账户并返回认证Token
     *
     * @param registerUsersDto 用户注册信息，包含用户名、密码、邮箱等
     * @return 包含JWT Token的Map结果
     * @throws Exception 注册过程中可能出现的异常
     */
    Map<String, String> registerUsers(RegisterUsersDto registerUsersDto) throws Exception;

    /**
     * 用户登录方法
     * 验证用户登录凭据，生成并返回认证Token
     *
     * @param dto 用户登录信息，包含用户名/手机号和密码
     * @return 包含JWT Token的Map结果
     */
    Map<String, String> loginUsers(LoginUserDto dto);

    /**
     * 发送邮箱验证码方法
     * 向指定邮箱发送验证码，用于邮箱验证或登录
     *
     * @param email 接收验证码的邮箱地址
     * @throws Exception 发送过程中可能出现的异常
     */
    void sendCodeByEmail(String email) throws Exception;

    /**
     * 验证邮箱验证码方法
     * 验证邮箱验证码的有效性
     *
     * @param code 待验证的验证码
     * @param email 验证码对应的邮箱地址
     * @return 验证结果，true表示验证成功
     */
    boolean validateEmailCode(String code, String email);

    /**
     * 发送手机验证码方法
     * 向指定手机号发送验证码，用于手机验证或登录
     *
     * @param phone 接收验证码的手机号
     * @return 包含发送结果的Map
     * @throws Exception 发送过程中可能出现的异常
     */
    void sendCodeByPhone(String phone) throws Exception;
}
