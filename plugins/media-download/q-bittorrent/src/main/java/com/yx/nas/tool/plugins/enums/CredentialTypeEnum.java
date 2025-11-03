package com.yx.nas.tool.plugins.enums;

import lombok.Getter;

/**
 * 认证凭据类型 1: cookie
 */
@Getter
public enum CredentialTypeEnum {
    COOKIE(1, "Cookie");

    private final int code;
    private final String description;

    CredentialTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
