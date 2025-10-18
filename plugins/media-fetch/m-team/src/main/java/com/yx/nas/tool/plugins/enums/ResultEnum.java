package com.yx.nas.tool.plugins.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    SUCCESS(10000, "成功"),
    SETTING_EXIST(20001, "已存在M-Team配置，无法新增，请修改后重新提交"),
    SETTING_NOT_EXIST(20002, "M-Team配置不存在，请检查");

    private final Integer code;
    private final String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
