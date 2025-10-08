package com.example.afanguserbackend.mapper.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.afanguserbackend.common.PageRequest;
import com.example.afanguserbackend.common.PageResponse;
import com.example.afanguserbackend.mapper.user.UsersMapper;
import com.example.afanguserbackend.model.dto.user.UsersDto;
import com.example.afanguserbackend.model.entity.user.Users;
import com.example.afanguserbackend.model.vo.user.UserVo;
import com.example.afanguserbackend.mapper.service.user.UsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

//业务实现
//UserVo 应该只包含用户ID、用户名、邮箱、创建时间等非敏感信息，密码字段应该完全不存在于VO中。

@Service
public class UserServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Override
    public UserVo addUser(UsersDto usersDto) {

        baseMapper.insert(Users.builder()
                .username(usersDto.getUsername())
                .password(usersDto.getPassword())
                .email(usersDto.getEmail())
                .build());
        UserVo userVo =new  UserVo();
        userVo.setUsername(usersDto.getUsername());
//        userVo.setPassword(usersDto.getPassword());密码不可以返回明文密码，需要加密
        return userVo;
    }

    @Override
    public UserVo getUserById(Long id) {
//        通过id查找用户信息
        Users user = baseMapper.selectById(id);//UsersMapper中已经继承了BaseMapper，可以直接使用selectById方法
        if (user == null) {//        如果用户不存在，返回null
            return null;
        }
        UserVo userVo = new UserVo();
        userVo.setUsername(user.getUsername());
        userVo.setEmail(user.getEmail());
        userVo.setCreateTime(user.getCreateTime());
//        userVo.setPassword(user.getPassword());密码不可以返回明文密码，需要加密
        return userVo;
        }

    @Override
    public UserVo getUserByUsername(String username) {
//        通过用户名查找用户信息
//  1. baseMapper.selectOne()：从数据库查询一条记录，返回 Users 对象
//  2. new QueryWrapper<Users>()：创建 MyBatis-Plus 的查询条件构造器
//  3. .eq("username", username)：添加等值查询条件，相当于 SQL 中的 WHERE username = ?
        Users user = baseMapper.selectOne(new QueryWrapper<Users>().eq("username", username));
        if (user == null) {//        如果用户不存在，返回null
            return null; }
        UserVo userVo = new UserVo();
        userVo.setUsername(user.getUsername());
        userVo.setEmail(user.getEmail());
        userVo.setCreateTime(user.getCreateTime());
        //        userVo.setPassword(user.getPassword());密码不可以返回明文密码，需要加密
        return userVo;
    }

    @Override
    public UserVo getUserByEmail(String email) {
//        通过邮箱查找用户信息
        Users user = baseMapper.selectOne(new QueryWrapper<Users>().eq("email", email));
        if (user == null) {//        如果用户不存在，返回null
        return null;
        }
        UserVo userVo = new UserVo();
        userVo.setUsername(user.getUsername());
        userVo.setEmail(user.getEmail());
        userVo.setCreateTime(user.getCreateTime());
        //        userVo.setPassword(user.getPassword());密码不可以返回明文密码，需要加密
        return userVo;
    }

    @Override
    public UserVo updateUser(UsersDto usersDto) {
//        更新用户信息
        Users user = baseMapper.selectOne(new QueryWrapper<Users>().eq("username", usersDto.getUsername()));
        if (user == null) {//        如果用户不存在，返回null
            return null; }
        user.setUsername(usersDto.getUsername());
        user.setEmail(usersDto.getEmail());
        user.setPhone(usersDto.getPhone());
        user.setUpdateTime(String.valueOf(LocalDateTime.now()));
        return null;
    }

    @Override
    public void deleteUser(Long id) {
//通过id删除用户
//        判断是否存在
        if (baseMapper.selectById(id) == null) {//        如果不存在，返回null
            return;}
        baseMapper.deleteById(id);
        }

    @Override
    public PageResponse<UserVo> getUsers(PageRequest pageRequest) {
//        获取用户信息
//        1.创建分页对象
        Page<Users> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        //        2.查询
        Page<Users> usersPage = baseMapper.selectPage(page, null);
        //        3.封装数据
        PageResponse<UserVo> pageResponse = new PageResponse<>();
        pageResponse.setRecords(usersPage.getRecords().stream().map(user -> {//            将Users对象转换为UserVo对象
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            return userVo;}
        ).collect(Collectors.toList()));
        pageResponse.setTotal(usersPage.getTotal());
        pageResponse.setPageNum(usersPage.getCurrent());
        return pageResponse;
    }

    @Override
    public void addusers(UsersDto dto){
        Users users = new Users();
        BeanUtils.copyProperties(dto, users);
        this.save(users);
    }
}
