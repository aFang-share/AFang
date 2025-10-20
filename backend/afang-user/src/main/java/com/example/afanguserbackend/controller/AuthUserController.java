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
 * 接收请求发送结果
 */

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class AuthUserController {
    private final AuthUserService authUserService;

    /**
     * 用户注册
     * @param registerUsersDto
     * @return 返回结果Token
     * @throws Exception
     */
    @PostMapping("/registerUser")
    public BaseResponse<Map<String,String>> registerUser(@Valid @RequestBody RegisterUsersDto registerUsersDto) throws Exception {
        //使用自定义返回,返回
        return ResultUtils.success(authUserService.registerUsers(registerUsersDto));
    }

    /**
     * 用户登录
     * @Validated
     * @param loginUserDto
     * @return 返回结果Token
     * @throws Exception
     * */

    @PostMapping("/loginUser")
    public BaseResponse<Map<String, String>> loginUser(@Valid @RequestBody LoginUserDto loginUserDto) {
        return ResultUtils.success(authUserService.loginUsers(loginUserDto));
    }

    /**
     * 发送邮箱验证码
     * @param email
     * @return 验证码发送状态
     * @throws Exception
     **/
    @GetMapping("/sendCodeByEmail")
    public BaseResponse<Void> sendCodeByEmail(String email) throws Exception {
        authUserService.sendCodeByEmail(email);
        return ResultUtils.success();
    }

    /**
     * 邮箱验证码验证，邮箱验证码登录
     * @param map
     * @return 验证状态
     */
    @PostMapping("/validateEmailCode")
    public BaseResponse<String> validateEmailCode(@RequestBody Map<String, String> map) {
        authUserService.validateEmailCode(map.get("code"),map.get("email"));
        return  ResultUtils.success("验证成功，请前往登录");
    }

    /**
     * 手机验证码验证，手机验证码登录
     * @param phone
     * @return验证状态
     * @throws Exception
     */
    @GetMapping("/validatePhoneCode")
    public BaseResponse<String> validatePhoneCode(@RequestParam String phone) throws Exception {
        authUserService.sendCodeByPhone(phone);
        return  ResultUtils.success("验证成功，请前往登录");
    }
}