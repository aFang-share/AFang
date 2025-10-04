package com.example.afanguserbackend.model.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("users")
public class Users {
    @TableField("key")
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String avatar;
//    private String role;
    private String status;
//    默认映射creat_time mysql字段
    private String createTime;
    private String updateTime;
}