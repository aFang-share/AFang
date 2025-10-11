package com.example.afanguserbackend.controller;

import com.example.afanguserbackend.common.BaseResponse;
import com.example.afanguserbackend.common.ResultUtils;
import com.example.afanguserbackend.model.dto.user.AddUsersDto;
import com.example.afanguserbackend.model.dto.user.LoginUserDto;
import com.example.afanguserbackend.model.dto.user.UpdateUsersDto;
import com.example.afanguserbackend.service.user.UsersService;
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
public class UserController {

    private final UsersService usersService;

    @PostMapping("/addUser")
    public BaseResponse<Void> addUser(@RequestBody AddUsersDto addUsersDto) {
        //使用自定义返回,返回
        return usersService.addUsers(addUsersDto) ? ResultUtils.success() : ResultUtils.fail("用户创建失败!");
    }

    @PostMapping("/loginUser")
    public BaseResponse<Map<String, String>> loginUser(@RequestBody LoginUserDto loginUserDto) {
        return ResultUtils.success(usersService.loginUsers(loginUserDto));
    }

    //    更新用户信息
    @PostMapping("/updateUser")
    public BaseResponse<Void> update(@RequestBody UpdateUsersDto updateUsersDto) {
        return usersService.updateUsers(updateUsersDto) ? ResultUtils.success() : ResultUtils.fail("用户信息更改失败!");
    }
}

