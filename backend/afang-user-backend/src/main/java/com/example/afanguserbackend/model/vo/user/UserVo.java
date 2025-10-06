package com.example.afanguserbackend.model.vo.user;
import lombok.Data;
import lombok.Setter;

//VO类用于封装数据
@Data
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
    private String createTime;

}
