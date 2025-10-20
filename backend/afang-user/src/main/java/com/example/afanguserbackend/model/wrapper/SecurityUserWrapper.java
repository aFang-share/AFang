package com.example.afanguserbackend.model.wrapper;

import com.example.afanguserbackend.model.entity.user.Users;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Spring Security用户包装类
 * 用于将自定义的Users实体包装为Spring Security的UserDetails实现
 * 支持基于手机号的用户认证
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@Data
public class SecurityUserWrapper implements UserDetails {

    /**
     * 用户实体对象
     * 包含用户的基本信息
     */
    private Users users;

    /**
     * 构造函数
     * @param users 用户实体对象
     */
    public SecurityUserWrapper(Users users) {
        this.users = users;
    }

      /**
     * 获取用户权限集合
     * @return 权限集合，当前返回空列表
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * 获取用户密码
     * @return 用户密码
     */
    @Override
    public String getPassword() {
        return users.getPassword();
    }

    /**
     * 获取用户名
     * 注意：这里使用手机号作为用户名进行认证
     * @return 用户手机号
     */
    @Override
    public String getUsername() {
        return users.getPhone();
    }

    /**
     * 账户是否未过期
     * @return false，表示账户已过期（需要根据实际业务逻辑调整）
     */
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    /**
     * 账户是否未锁定
     * @return false，表示账户已锁定（需要根据实际业务逻辑调整）
     */
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    /**
     * 凭证是否未过期
     * @return false，表示凭证已过期（需要根据实际业务逻辑调整）
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /**
     * 账户是否可用
     * @return false，表示账户不可用（需要根据实际业务逻辑调整）
     */
    @Override
    public boolean isEnabled() {
        return false;
    }

}
