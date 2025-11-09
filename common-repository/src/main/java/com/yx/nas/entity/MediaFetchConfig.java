package com.yx.nas.entity;

import com.yx.nas.entity.base.BaseEntity;
import org.jetbrains.annotations.Nullable;
import org.babyfish.jimmer.sql.*;

import java.util.List;


/**
 * <p>
 * 下载站点认证信息，用于访问资源下载站点
 * </p>
 *
 * @author yx
 * @date 2025-10-07
 */
@Entity
@Table(name = "media_fetch_config")
public interface MediaFetchConfig extends BaseEntity {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    /**
     * 站点名称
     */
    @Column(name = "name")
    String name();


    /**
     * 访问密钥
     */
    @Column(name = "url")
    String url();

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
    @Key
    int authType();

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

    /**
     * 插件ID
     */
    @Column(name = "plugin_id")
    @Key
    String pluginId();

    /**
     * 关联的下载工具配置
     */
    @ManyToMany
    @JoinTable(
            name = "media_fetch_download_rel",
            joinColumnName = "download_tool_id",
            inverseJoinColumnName = "media_fetch_id"
    )
    List<DownloadToolConfig> downloadToolConfigs();

}
