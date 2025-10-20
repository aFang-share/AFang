package com.example.afanguserbackend.model.vo.user;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 用户视图对象
 * 用于向前端返回用户信息，隐藏敏感数据如密码
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
    /**
     * 用户名
     * 用户登录名
     */
    private String username;

    /**
     * 密码
     * 注意：通常在VO中不应包含密码字段，这里保留但实际使用时应考虑安全性
     */
    private String password;

    /**
     * 邮箱
     * 用户邮箱地址
     */
    private String email;

    /**
     * 手机号
     * 用户绑定的手机号码
     */
    private String phone;

    /**
     * 头像
     * 用户头像URL或路径
     */
    private String avatar;

    /**
     * 用户状态
     * 账户当前状态
     */
    private String status;

    /**
     * 用户角色
     * 用户权限角色
     */
    private String userRole;

    /**
     * 创建时间
     * 账户创建时间
     */
    @Setter
    private LocalDateTime createTime;

    /**
     * 更新时间
     * 账户最后更新时间
     */
    @Setter
    private LocalDateTime updateTime;

}
