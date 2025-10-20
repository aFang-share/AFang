package com.example.afanguserbackend.model.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库中的users表，存储用户基本信息
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("users")
public class Users implements Serializable {
      /**
     * 序列化版本号
     * 用于Serializable接口的版本控制
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     * 主键，自增长
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     * 用户登录的唯一标识符
     */
    private String username;

    /**
     * 密码
     * 用户登录密码，存储加密后的值
     */
    private String password;

    /**
     * 邮箱
     * 用户邮箱地址，用于接收通知和验证
     */
    private String email;

    /**
     * 手机号
     * 用户绑定的手机号码
     */
    private String phone;

    /**
     * 头像
     * 用户头像的URL或文件路径
     */
    private String avatar;

    /**
     * 用户角色
     * 用户权限角色标识（如：admin、user等）
     */
    private String userRole;

    /**
     * 用户状态
     * 账户状态标识（如：active、inactive、banned等）
     */
    private String status;

    /**
     * 创建时间
     * 记录创建时间，插入时自动填充
     */
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     * 记录最后更新时间，插入和更新时自动填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}