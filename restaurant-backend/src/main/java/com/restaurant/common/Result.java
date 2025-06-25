package com.restaurant.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回结果类，服务端响应的数据最终都会封装成此对象
 * @param <T> 数据类型
 */
@Data
public class Result<T> implements Serializable {

    private Integer code; // 编码：1成功，0和其它数字为失败
    private String msg; // 错误信息
    private T data; // 数据

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = 1;
        result.msg = "操作成功";
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<>();
        result.data = object;
        result.code = 1;
        result.msg = "操作成功";
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.msg = msg;
        result.code = 0;
        return result;
    }

    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.msg = msg;
        result.code = code;
        return result;
    }
}