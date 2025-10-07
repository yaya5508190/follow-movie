package com.yx.nas.entity;

import java.time.LocalDateTime;

import com.yx.nas.entity.base.BaseEntity;
import org.jetbrains.annotations.Nullable;
import org.babyfish.jimmer.sql.*;


/**
 * <p>
 * 下载站点认证信息，用于访问资源下载站点
 * </p>
 *
 * @author yx
 * @date 2025-10-07
 */
@Entity
@Table(name = "media_fetch_auth")
public interface MediaFetchAuth extends BaseEntity {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    /**
     * 访问密钥
     */
    @Column(name = "api_key")
    @Nullable
    String apiKey();

    /**
     * 访问用户名
     */
    @Column(name = "user_name")
    @Nullable
    String userName();

    /**
     * 访问密码
     */
    @Nullable
    String password();

    /**
     * 认证cookie
     */
    @Nullable
    String authCookie();

    /**
     * 1:api_key 2: 用户名密码
     */
    @Column(name = "auth_type")
    @Nullable
    Integer authType();

    /**
     * 来源站点
     */
    @Column(name = "fetcher_source")
    String fetcherSource();

    /**
     * 额外元数据
     */
    @Column(name = "extra_metainfo")
    @Nullable
    String extraMetainfo();

}
