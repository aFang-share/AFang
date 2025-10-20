package com.example.afanguserbackend.model.dto.user.common_user_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户更新数据传输对象
 * 用于接收用户信息更新时提交的数据，支持部分字段更新
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUsersDto {
    /**
     * 用户ID
     * 要更新的用户唯一标识符
     */
    private Long id;

    /**
     * 用户名
     * 用户登录名，可选更新字段
     */
    private String username;

    /**
     * 密码
     * 用户登录密码，可选更新字段，更新时需要加密处理
     */
    private String password;

    /**
     * 邮箱
     * 用户邮箱地址，可选更新字段
     */
    private String email;

    /**
     * 手机号
     * 用户绑定的手机号码，可选更新字段
     */
    private String phone;

    /**
     * 头像
     * 用户头像URL或路径，可选更新字段
     */
    private String avatar;

    /**
     * 用户状态
     * 用户账户状态（如：正常、禁用等），可选更新字段
     */
    private String status;

    /**
     * 用户角色
     * 用户权限角色（如：管理员、普通用户等），可选更新字段
     */
    private String userRole;
}
