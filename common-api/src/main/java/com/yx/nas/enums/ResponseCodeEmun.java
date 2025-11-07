package com.yx.nas.enums;

import lombok.Getter;

/**
 * 响应状态码枚举
 *
 * @author yx
 * @date 2025-11-05
 */
@Getter
public enum ResponseCodeEmun {

    /**
     * 成功
     */
    SUCCESS(10000, "操作成功"),

    /**
     * 客户端错误 - 请求参数错误
     */
    BAD_REQUEST(400, "请求参数错误"),

    /**
     * 客户端错误 - 未认证
     */
    UNAUTHORIZED(401, "未认证，请先登录"),

    /**
     * 客户端错误 - 无权限
     */
    FORBIDDEN(403, "无权限访问"),

    /**
     * 服务器错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),

    /**
     * 业务错误 - 用户已存在
     */
    USER_ALREADY_EXISTS(40001, "用户已存在"),

    /**
     * 业务错误 - 用户不存在
     */
    USER_NOT_FOUND(40002, "用户不存在"),

    /**
     * 业务错误 - 用户名或密码错误
     */
    LOGIN_FAILED(40003, "用户名或密码错误");

    private final Integer code;
    private final String message;

    ResponseCodeEmun(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
