package com.yx.nas.entity;

import com.yx.nas.entity.base.BaseEntity;
import org.jetbrains.annotations.Nullable;
import org.babyfish.jimmer.sql.*;

import java.util.List;


/**
 * <p>
 * download_tool_info
 * </p>
 *
 * @author yx
 * @date 2025-10-08
 */
@Entity
@Table(name = "download_tool_config")
public interface DownloadToolConfig extends BaseEntity {

    /**
     * id主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    /**
     * 名称
     */
    String name();

    /**
     * 认证类型 1 用户名密码 2 cookie
     */
    @Column(name = "auth_type")
    int authType();

    /**
     * 工具类型 q-bittorrent
     */
    String type();

    /**
     * 访问地址
     */
    String url();

    /**
     * 用户名
     */
    @Nullable
    String username();

    /**
     * 密码
     */
    @Nullable
    String password();

    /**
     * Cookie
     */
    @Nullable
    String cookie();

    /**
     * 默认保存路径
     */
    @Column(name = "save_path")
    String savePath();

    /**
     * 是否默认
     */
    @Column(name = "default_tool")
    boolean defaultTool();

    @ManyToMany(mappedBy = "downloadToolConfigs", orderedProps = {
            @OrderedProp("name"),
    })
    List<MediaFetchConfig> mediaFetchConfigs();

    /**
     * 关联的下载工具配置
     */
    @Nullable
    @ManyToOne
    @JoinTable(
            name = "pre_auth_download_rel",
            joinColumnName = "download_tool_id",
            inverseJoinColumnName = "pre_auth_id"
    )
    @OnDissociate(DissociateAction.SET_NULL)
    SysPreAuth sysPreAuth();
}
