package com.yx.nas.model.common;

import com.yx.nas.enums.ResponseCodeEmun;
import lombok.Data;

@Data
public class CommonResult<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(ResponseCodeEmun.SUCCESS.getCode());
        result.setMessage(ResponseCodeEmun.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    public static <T> CommonResult<T> success() {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(ResponseCodeEmun.SUCCESS.getCode());
        result.setMessage(ResponseCodeEmun.SUCCESS.getMessage());
        return result;
    }

    public static <T> CommonResult<T> success(T data, String message) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(ResponseCodeEmun.SUCCESS.getCode());
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <T> CommonResult<T> successWithMessage(String message) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(ResponseCodeEmun.SUCCESS.getCode());
        result.setMessage(message);
        return result;
    }

    public static <T> CommonResult<T> failure(Integer code, String message) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> CommonResult<T> failure(ResponseCodeEmun responseCode) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(responseCode.getCode());
        result.setMessage(responseCode.getMessage());
        return result;
    }

    public static <T> CommonResult<T> failure(ResponseCodeEmun responseCode, String customMessage) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(responseCode.getCode());
        result.setMessage(customMessage);
        return result;
    }
}