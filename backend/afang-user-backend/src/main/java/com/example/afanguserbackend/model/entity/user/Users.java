package com.example.afanguserbackend.model.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
//    private String role;
    private String status;
//    默认映射creat_time mysql字段
    private String createTime;
    private String updateTime;
}