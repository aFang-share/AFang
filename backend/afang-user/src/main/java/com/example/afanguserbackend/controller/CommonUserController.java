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
 * 用户管理控制器
 * 提供用户信息更新等普通业务操作的API接口
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class CommonUserController {

    /**
     * 用户业务服务接口
     */
    private final CommonUserService commonUserService;

    /**
     * 更新用户信息接口
     * 根据提供的用户信息更新对应用户的数据
     *
     * @param updateUsersDto 用户更新信息，包含需要更新的字段
     * @return 更新操作结果，成功返回空数据，失败返回错误信息
     */
    @PostMapping("/updateUser")
    public BaseResponse<Void> update(@RequestBody UpdateUsersDto updateUsersDto) {
        return commonUserService.updateUsers(updateUsersDto) ?
                ResultUtils.success()
                : ResultUtils.fail("用户信息更改失败!");
    }
}
