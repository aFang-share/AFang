package com.example.afanguserbackend.controller;

import com.example.afanguserbackend.common.BaseResponse;
import com.example.afanguserbackend.common.ResultUtils;
import com.example.afanguserbackend.model.dto.user.common_user_dto.UpdateUsersDto;
import com.example.afanguserbackend.service.user.CommonUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST controller for handling user-related operations
 * Provides endpoints for user management functionality
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
/**
 * 用户普通业务
 */
public class CommonUserController {
    /**
     * 注入CommonUserService
     */
    private final CommonUserService commonUserService;

    /**
     * 更新用户信息
     * @param updateUsersDto
     * @return BaseResponse<Void>
     */
    @PostMapping("/updateUser")
    public BaseResponse<Void> update(@RequestBody UpdateUsersDto updateUsersDto) {
        return commonUserService.updateUsers(updateUsersDto) ?
                ResultUtils.success()
                : ResultUtils.fail("用户信息更改失败!");
    }
}
