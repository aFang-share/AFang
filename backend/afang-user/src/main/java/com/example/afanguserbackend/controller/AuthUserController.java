package com.example.afanguserbackend.controller;

import com.example.afanguserbackend.common.BaseResponse;
import com.example.afanguserbackend.common.ResultUtils;
import com.example.afanguserbackend.model.dto.user.auth_user_dto.LoginUserDto;
import com.example.afanguserbackend.model.dto.user.auth_user_dto.RegisterUsersDto;
import com.example.afanguserbackend.service.user.AuthUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

//控制器接受请求发送结果
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class AuthUserController {
    private final AuthUserService authUserService;
    @PostMapping("/registerUser")
    public BaseResponse<Void> registerUser(@RequestBody RegisterUsersDto registerUsersDto) {
        //使用自定义返回,返回
        return authUserService.registerUsers(registerUsersDto) ? ResultUtils.success() : ResultUtils.fail("用户创建失败!");
    }

    @PostMapping("/loginUser")
    public BaseResponse<Map<String, String>> loginUser(@RequestBody LoginUserDto loginUserDto) {
        return ResultUtils.success(authUserService.loginUsers(loginUserDto));
    }

}
