package com.example.afanguserbackend.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.afanguserbackend.model.entity.user.Users;

/**
 * 用户数据访问层接口
 * 提供用户实体的数据库操作功能
 * 继承MyBatis-Plus的BaseMapper，自动拥有CRUD功能
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
public interface UsersMapper extends BaseMapper<Users> {
    // 继承BaseMapper后，自动拥有以下方法：
    // - insert: 插入记录
    // - deleteById: 根据ID删除
    // - updateById: 根据ID更新
    // - selectById: 根据ID查询
    // - selectList: 根据条件查询列表
    // - selectPage: 分页查询
    // 等更多MyBatis-Plus提供的通用方法
}
