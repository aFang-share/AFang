package com.example.afanguserbackend.model.vo.user;

import lombok.*;

import java.time.LocalDateTime;

//VO类用于封装数据
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * 用户信息
 * @param username 用户名
 * @param password 密码
 * @param email 邮箱
 * @param phone 手机号
 * @param avatar 头像
 * @param status 用户状态
 * @param userRole 用户角色
 * @param createTime 创建时间
 * @param updateTime 更新时间
 */
public class UserVo {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String avatar;
    private String status;
    private String userRole;
    //        将createTime转换为Date类型
    @Setter
    private LocalDateTime createTime;
    @Setter
    private LocalDateTime updateTime;

}
