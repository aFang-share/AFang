package com.example.afanguserbackend.controller;

import com.example.afanguserbackend.common.BaseResponse;
import com.example.afanguserbackend.common.ResultUtils;
import com.example.afanguserbackend.model.dto.user.auth_user_dto.LoginUserDto;
import com.example.afanguserbackend.model.dto.user.auth_user_dto.RegisterUsersDto;
import com.example.afanguserbackend.service.user.AuthUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户认证控制器
 * 提供用户注册、登录、验证码发送等认证相关的API接口
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class AuthUserController {

    /**
     * 用户认证服务接口
     */
    private final AuthUserService authUserService;

    /**
     * 用户注册接口
     * 接收用户注册信息，验证后创建新用户账户
     *
     * @param registerUsersDto 用户注册信息，包含用户名、密码、邮箱等
     * @return 包含JWT Token的响应结果
     * @throws Exception 注册过程中可能出现的异常
     */
    @PostMapping("/registerUser")
    public BaseResponse<Map<String,String>> registerUser(@Valid @RequestBody RegisterUsersDto registerUsersDto) throws Exception {
        return ResultUtils.success(authUserService.registerUsers(registerUsersDto));
    }

    /**
     * 用户登录接口
     * 验证用户登录凭据，返回认证Token
     *
     * @param loginUserDto 用户登录信息，包含用户名/手机号和密码
     * @return 包含JWT Token的响应结果
     */
    @PostMapping("/loginUser")
    public BaseResponse<Map<String, String>> loginUser(@Valid @RequestBody LoginUserDto loginUserDto) {
        return ResultUtils.success(authUserService.loginUsers(loginUserDto));
    }

    /**
     * 发送邮箱验证码接口
     * 向指定邮箱发送验证码，用于邮箱验证或登录
     *
     * @param email 接收验证码的邮箱地址
     * @return 发送结果响应
     * @throwsException 发送过程中可能出现的异常
     */
    @GetMapping("/sendCodeByEmail")
    public BaseResponse<Void> sendCodeByEmail(String email) throws Exception {
        authUserService.sendCodeByEmail(email);
        return ResultUtils.success();
    }

    /**
     * 邮箱验证码验证接口
     * 验证邮箱验证码的有效性，用于邮箱验证码登录
     *
     * @param map 包含验证码和邮箱的请求体参数
     * @return 验证结果消息
     */
    @PostMapping("/validateEmailCode")
    public BaseResponse<String> validateEmailCode(@RequestBody Map<String, String> map) {
        authUserService.validateEmailCode(map.get("code"), map.get("email"));
        return ResultUtils.success("验证成功，请前往登录");
    }

    /**
     * 发送手机验证码接口
     * 向指定手机号发送验证码，用于手机验证或登录
     *
     * @param phone 接收验证码的手机号
     * @return 发送结果响应
     * @throws Exception 发送过程中可能出现的异常
     */
    @GetMapping("/validatePhoneCode")
    public BaseResponse<String> validatePhoneCode(@RequestParam String phone) throws Exception {
        authUserService.sendCodeByPhone(phone);
        return ResultUtils.success("验证成功，请前往登录");
    }
}