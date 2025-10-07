package com.yx.nas.entity;

import java.time.LocalDateTime;

import com.yx.nas.entity.base.BaseEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.babyfish.jimmer.sql.*;


/**
 * <p>
 * 种子下载记录表
 *
 * </p>
 *
 * @author 35737
 * @date 2025-10-07
 */
@Entity
@Table(name = "media_torrent_record")
public interface MediaTorrentRecord extends BaseEntity {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    /**
     * resource_id
     */
    @Key
    @Column(name = "resource_id")
    String resourceId();

    /**
     * 来源站点
     */
    @Key
    @Column(name = "fetcher_source")
    String fetcherSource();

    /**
     * torrent_url
     */
    @Column(name = "torrent_url")
    String torrentUrl();

    /**
     * 种子状态 1: 待下载  2:已下载
     */
    @Column(name = "torrent_status")
    int torrentStatus();
}
