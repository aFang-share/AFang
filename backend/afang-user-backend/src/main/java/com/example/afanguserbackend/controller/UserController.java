package com.example.afanguserbackend.controller;

import com.example.afanguserbackend.model.dto.user.UsersDto;
import com.example.afanguserbackend.model.vo.user.UserVo;
import com.example.afanguserbackend.service.user.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//控制器接受请求发送结果
@RestController
@RequestMapping ("/user")
@RequiredArgsConstructor
public class UserController {

    private final UsersService usersService;

    @PostMapping("/addUser")
    public ResponseEntity<UserVo> addUser(@RequestBody UsersDto usersDto){
        return ResponseEntity.ok(usersService.addUser(usersDto));
    }
//    requestBody 请求体注解，表单转json
}

