package com.yx.nas.enums;

import lombok.Getter;

/**
 * 种子状态 1: 待下载  2:已下载
 */
@Getter
public enum TorrentStatusEnum {
    PENDING(1, "待下载"),
    DOWNLOADED(2, "已下载"),
    ;

    private final int code;
    private final String desc;

    TorrentStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
