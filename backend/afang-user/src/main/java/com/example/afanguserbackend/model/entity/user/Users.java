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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("users")

/**
 * 用户实体类
 * @param id 用户id
 * @param username 用户名
 * @param password 密码
 * @param email 邮箱
 * @param phone 手机号
 * @param avatar 头像
 * @param userRole 用户角色
 * @param status 用户状态
 * @createTime 创建时间
 * @updateTime 更新时间
 *
 */
public class Users implements Serializable {
    //    序列化
    @Serial
//    序列化版本号
    private static final long serialVersionUID = 1L;
    //    自增
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String avatar;
    private String userRole;
    private String status;
    //    默认映射creat_time mysql字段
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime updateTime;
}