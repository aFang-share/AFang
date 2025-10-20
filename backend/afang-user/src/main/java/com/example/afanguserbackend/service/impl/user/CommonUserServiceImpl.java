package com.example.afanguserbackend.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.afanguserbackend.mapper.user.UsersMapper;
import com.example.afanguserbackend.model.dto.user.common_user_dto.UpdateUsersDto;
import com.example.afanguserbackend.model.entity.user.Users;
import com.example.afanguserbackend.model.wrapper.SecurityUserWrapper;
import com.example.afanguserbackend.service.user.CommonUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户业务服务实现类
 * 实现用户信息管理功能，同时作为Spring Security的UserDetailsService
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CommonUserServiceImpl extends ServiceImpl<UsersMapper, Users> implements CommonUserService, UserDetailsService {

    /**
     * 更新用户信息实现
     * 根据提供的用户信息更新对应用户的数据
     *
     * @param dto 用户更新信息，包含需要更新的字段
     * @return 更新操作结果，true表示更新成功
     */
    @Override
    public boolean updateUsers(UpdateUsersDto dto) {
        // 查询现有用户信息
        Users existingUser = baseMapper.selectOne(new QueryWrapper<Users>().eq("id", dto.getId()));
        if (existingUser == null) {
            log.warn("尝试更新不存在的用户，用户ID：{}", dto.getId());
            return false;
        }

        // 创建更新对象并复制属性
        Users updateUser = new Users();
        BeanUtils.copyProperties(dto, updateUser);

        // 执行更新操作
        boolean updateResult = this.updateById(updateUser);

        if (updateResult) {
            log.info("用户信息更新成功，用户ID：{}", dto.getId());
        } else {
            log.error("用户信息更新失败，用户ID：{}", dto.getId());
        }

        return updateResult;
    }

    /**
     * Spring Security用户加载实现
     * 根据手机号加载用户详情，用于认证
     *
     * @param phone 用户手机号
     * @return Spring Security的UserDetails实现
     * @throws UsernameNotFoundException 用户不存在时抛出
     */
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<Users>()
                .eq(Users::getPhone, phone);

        Users user = baseMapper.selectOne(queryWrapper);
        if (user == null) {
            log.warn("根据手机号未找到用户，手机号：{}", phone);
            throw new UsernameNotFoundException("用户不存在");
        }

        log.debug("成功加载用户详情，用户ID：{}，手机号：{}", user.getId(), phone);
        return new SecurityUserWrapper(user);
    }
}
