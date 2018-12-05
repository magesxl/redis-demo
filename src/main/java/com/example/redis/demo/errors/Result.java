package com.example.redis.demo.errors;

/**
 * 统一API响应结果封装
 */
public class Result {
    private String code;
    private String message;
    private Object data;

    public Result setCode(ResultCode resultCode) {
        this.code = resultCode.code;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Result setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        if (data == null) {
            return "";
        }
        return data;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }

    public Long getTimestamp() {
        return System.currentTimeMillis();
    }
}