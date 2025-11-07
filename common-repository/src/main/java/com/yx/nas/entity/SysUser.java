package com.yx.nas.entity;

import com.yx.nas.entity.base.BaseEntity;
import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

/**
 * 系统用户实体
 *
 * @author yx
 * @date 2025-11-05
 */
@Entity
@Table(name = "sys_user")
public interface SysUser extends BaseEntity {

    /**
     * 用户ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    /**
     * 用户名
     */
    @Column(name = "username")
    @Key
    String userName();

    /**
     * 密码(已加密)
     */
    @Column(name = "password")
    String password();

    /**
     * 昵称
     */
    @Column(name = "nickname")
    @Nullable
    String nickName();

    /**
     * 邮箱
     */
    @Column(name = "email")
    @Nullable
    String email();

    /**
     * 是否启用
     */
    @Column(name = "enabled")
    boolean enabled();
}

