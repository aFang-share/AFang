package com.example.afanguserbackend.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.afanguserbackend.common.PageRequest;
import com.example.afanguserbackend.common.PageResponse;
import com.example.afanguserbackend.mapper.user.UsersMapper;
import com.example.afanguserbackend.model.dto.user.AddUsersDto;
import com.example.afanguserbackend.model.dto.user.LoginUserDto;
import com.example.afanguserbackend.model.entity.user.Users;
import com.example.afanguserbackend.model.vo.user.UserVo;
import com.example.afanguserbackend.service.user.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

//业务实现
//UserVo 应该只包含用户ID、用户名、邮箱、创建时间等非敏感信息，密码字段应该完全不存在于VO中。

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserVo addUser(AddUsersDto addUsersDto) {
        return null;
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
    public UserVo updateUser(AddUsersDto addUsersDto) {
        return null;
    }

    @Override
    public UserVo upDateUser(AddUsersDto addUsersDto) {
//        更新用户信息
        Users user = baseMapper.selectOne(new QueryWrapper<Users>().eq("username", addUsersDto.getUsername()));
        if (user == null) {//        如果用户不存在，返回null
            return null; }
        user.setUsername(addUsersDto.getUsername());
        user.setEmail(addUsersDto.getEmail());
        user.setPhone(addUsersDto.getPhone());
        user.setUpdateTime(LocalDateTime.now());
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
    public boolean addUsers(AddUsersDto dto){
//        TODO：代码优化
        Users users = new Users();
        Users user = baseMapper.selectOne(new QueryWrapper<Users>().eq("username", dto.getUsername()));
        if (user==null){//            用户名不存在
            BeanUtils.copyProperties(dto, users);
            users.setPassword(passwordEncoder.encode(dto.getPassword()));
            return this.save(users);
        }else {
            return false;
            }
    }

    @Override
    public boolean loginUsers(AddUsersDto addUsersDto) {
        return false;
    }

    @Override
    public boolean loginUsers(LoginUserDto dto){
        Users users = new Users();
        Users user = baseMapper.selectOne(new QueryWrapper<Users>().eq("username", dto.getUsername()));
        if (user==null){
            return false;

        }else {
//用户存在验证密码
            return passwordEncoder.matches(dto.getPassword(),user.getPassword());
        }
    }
}
