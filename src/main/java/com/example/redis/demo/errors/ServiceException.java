package com.example.redis.demo.errors;

/**
 * 服务（业务）异常
 */
public class ServiceException extends RuntimeException {

    private ResultCode resultCode;
    private String code;

    public ServiceException() {
    }

    public ServiceException(ResultCode code, String message) {
        super(message);
        this.resultCode = code;
    }

    public ServiceException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public String getCode() {
        return code;
    }
}