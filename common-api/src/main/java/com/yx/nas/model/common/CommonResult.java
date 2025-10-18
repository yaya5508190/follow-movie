package com.yx.nas.model.common;

import lombok.Data;

@Data
public class CommonResult<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(10000);
        result.setData(data);
        return result;
    }

    public  static <T> CommonResult<T> success() {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(10000);
        return result;
    }

    public static <T> CommonResult<T> failure(Integer code, String message) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
