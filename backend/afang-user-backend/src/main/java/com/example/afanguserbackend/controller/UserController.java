package com.example.afanguserbackend.controller;

import com.example.afanguserbackend.common.BaseResponse;
import com.example.afanguserbackend.common.ResultUtils;
import com.example.afanguserbackend.enums.StatusCode;
import com.example.afanguserbackend.model.dto.user.UsersDto;
import com.example.afanguserbackend.mapper.service.user.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//控制器接受请求发送结果
@RestController
@RequestMapping ("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UsersService usersService;

    @PostMapping("/addUser")
    public BaseResponse<Void> addUser(@RequestBody UsersDto usersDto) {
//        service里需要用 this.usersService.addusers(usersDto);
        try {
            usersService.addusers(usersDto);
        } catch (Exception e) {
            //            异常打印日志
            log.error("添加用户失败", e);
            return ResultUtils.fail(StatusCode.SYSTEM_ERROR);
        }

//        添加用户信息接口应该返回创建的用户信息
//        return ResponseEntity.status(HttpStatus.OK).build(); 返回的结构过于简易只有状态码
//        其他方法固定了状态return ResponseEntity.ok().build();
//        return ResponseEntity.ok().build();仅仅返回了状态码
//        return new BaseResponse<>(0,null,"新用户添加成功"); 结构化不标准，不允许，建议使用ResultUtils
        return ResultUtils.success();
    }
}

