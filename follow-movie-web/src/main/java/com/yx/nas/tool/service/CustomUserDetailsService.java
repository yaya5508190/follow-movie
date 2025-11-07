package com.yx.nas.tool.service;


import com.yx.nas.dto.SysUserDetailView;
import com.yx.nas.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

/**
 * 用户认证服务
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SysUserRepository sysUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 从数据库查询用户 (这里指定了 username 列)
        SysUserDetailView user = sysUserRepository.findByUserName(username);
         if(Objects.isNull(user)){
                throw new UsernameNotFoundException("用户不存在");
         }

        // 2. 转换为 Spring Security 的 UserDetails
        return User.builder()
                .username(user.getUserName())      // ← 指定用户名来自哪一列
                .password(user.getPassword())      // ← 指定密码来自哪一列
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_USER") // 用户权限
                ))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
