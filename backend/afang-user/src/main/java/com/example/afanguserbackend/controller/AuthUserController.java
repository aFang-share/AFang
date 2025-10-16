package com.example.afanguserbackend.controller;

import com.example.afanguserbackend.common.BaseResponse;
import com.example.afanguserbackend.common.ResultUtils;
import com.example.afanguserbackend.model.dto.user.auth_user_dto.LoginUserDto;
import com.example.afanguserbackend.model.dto.user.auth_user_dto.RegisterUsersDto;
import com.example.afanguserbackend.service.user.AuthUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//控制器接受请求发送结果
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class AuthUserController {
    private final AuthUserService authUserService;

    @PostMapping("/registerUser")
    public BaseResponse<Map<String,String>> registerUser(@RequestBody RegisterUsersDto registerUsersDto) throws Exception {
        //使用自定义返回,返回
        return ResultUtils.success(authUserService.registerUsers(registerUsersDto));
    }

    @PostMapping("/loginUser")
    public BaseResponse<Map<String, String>> loginUser(@RequestBody LoginUserDto loginUserDto) {
        return ResultUtils.success(authUserService.loginUsers(loginUserDto));
    }

    @GetMapping("/sendCodeByEmail")
    public BaseResponse<Void> sendCodeByEmail(String email) throws Exception {
        authUserService.sendCodeByEmail(email);
        return ResultUtils.success();
    }

    /**
     * 邮箱验证码验证，邮箱验证码登录
     */
    @PostMapping("/validateEmailCode")
    public BaseResponse<String> validateEmailCode(@RequestBody Map<String, String> map) {
        authUserService.validateEmailCode(map.get("code"),map.get("email"));
        return  ResultUtils.success("验证成功，请前往登录");
    }
}