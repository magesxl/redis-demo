package com.example.redis.demo.errors;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "ok";

    public static Result genSuccessResult() {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static Result genSuccessResult(Object data) {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result genSuccessResult(ResultCode resultCode, String message,Object data) {
        return new Result()
                .setCode(resultCode)
                .setMessage(message)
                .setData(data);
    }

    public static Result genFailResult(ResultCode resultCode, String message) {
        return new Result()
                .setCode(resultCode)
                .setMessage(message)
                .setData("");
    }

    public static Result genFailResult(String resultCode, String message) {
        return new Result()
                .setCode(resultCode)
                .setMessage(message)
                .setData("");
    }


}