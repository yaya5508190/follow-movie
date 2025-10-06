package com.yx.nas.entity;

import java.time.LocalDateTime;

import org.jetbrains.annotations.Nullable;
import org.babyfish.jimmer.sql.*;


/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author yx
 * @date 2025-10-07
 */
@Entity
@Table(name = "sys_config")
public interface SysConfig {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    /**
     * 配置类型
     */
    @Key
    @Column(name = "config_type")
    String configType();

    /**
     * 配置名称
     */
    @Column(name = "config_name")
    String configName();

    /**
     * 配置键
     */
    @Key
    @Column(name = "config_key")
    String configKey();

    /**
     * 配置值
     */
    @Column(name = "config_value")
    String configValue();

    /**
     * 备注
     */
    @Nullable
    String remark();

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @Nullable
    LocalDateTime createTime();

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @Nullable
    LocalDateTime updateTime();

}
