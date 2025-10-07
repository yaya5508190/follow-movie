package com.yx.nas.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthTypeEnum {
    /**
     * API Key
     */
    API_KEY(1, "API Key"),

    /**
     * 账号密码
     */
    USERNAME_PASSWORD(2, "账号密码"),

    /**
     * Cookie
     */
    COOKIE(3, "Cookie");

    private final Integer code;
    private final String description;

    /**
     * 根据code获取枚举
     */
    public static AuthTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (AuthTypeEnum type : AuthTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
