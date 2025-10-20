package com.example.afanguserbackend.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.afanguserbackend.model.dto.user.common_user_dto.UpdateUsersDto;
import com.example.afanguserbackend.model.entity.user.Users;

/**
 * 用户业务服务接口
 * 提供用户信息管理等普通业务功能
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
public interface CommonUserService extends IService<Users> {

    /**
     * 更新用户信息方法
     * 根据提供的用户信息更新对应用户的数据
     *
     * @param dto 用户更新信息，包含需要更新的字段
     * @return 更新操作结果，true表示更新成功
     */
    boolean updateUsers(UpdateUsersDto dto);
}
