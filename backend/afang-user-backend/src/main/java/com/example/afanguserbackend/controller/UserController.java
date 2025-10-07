package com.example.afanguserbackend.controller;

import com.example.afanguserbackend.model.dto.user.UsersDto;
import com.example.afanguserbackend.service.user.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Void> addUser(@RequestBody UsersDto usersDto) {
//        service里需要用 this.usersService.addusers(usersDto);
        usersService.addusers(usersDto);
        return ResponseEntity.status(HttpStatus.OK).build();
//        其他方法固定了状态return ResponseEntity.ok().build();
    }
}

