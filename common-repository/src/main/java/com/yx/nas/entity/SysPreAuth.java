package com.yx.nas.entity;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;
import com.yx.nas.entity.base.BaseEntity;
import org.babyfish.jimmer.sql.*;


/**
 * <p>
 * sys_pre_auth
 * </p>
 *
 * @author yx
 * @date 2025-10-22
 */
@Entity
@Table(name = "sys_pre_auth")
public interface SysPreAuth extends BaseEntity {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    /**
     * auth_name
     */
    @Column(name = "auth_name")
    String authName();

    /**
     * 认证URL
     */
    @Column(name = "auth_url")
    @Nullable
    String authUrl();

    /**
     * 访问用户名
     */
    @Column(name = "user_name")
    @Nullable
    String userName();

    /**
     * 访问密码
     */
    @Column(name = "password")
    @Nullable
    String password();

    /**
     * '0:无 1:api_key 2: 用户名密码 ';
     */
    @Column(name = "auth_type")
    int authType();

    /**
     * 认证凭据
     */
    @Column(name = "credential")
    @Nullable
    String credential();

    /**
     * 认证凭据类型1: cookie
     */
    @Column(name = "credential_type")
    int credentialType();

    /**
     * 额外元数据
     */
    @Column(name = "extra_metainfo")
    @Nullable
    @Serialized
    JsonNode extraMetainfo();

    /**
     * 插件id
     */
    @Column(name = "plugin_id")
    String pluginId();

}
