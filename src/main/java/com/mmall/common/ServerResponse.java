package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

// 该注解表示，结果为 null 的值，不会序列化到 json 中
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {

    private int status;
    private String message;
    private T data;

    private ServerResponse(int status) {
        this(status, null, null);
    }

    private ServerResponse(int status, String message) {
        this(status, message, null);
    }

    private ServerResponse(int status, T data) {
        this(status, null, data);
    }

    private ServerResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // 加上这个注解，那么序列化 json 的时候，不会包括这个字段
    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String message) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), message);
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createBySuccess(String message, T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), message, data);
    }

    public static <T> ServerResponse<T> createByError() {
        return new ServerResponse<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage) {
        return new ServerResponse<>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new ServerResponse<>(errorCode, errorMessage);
    }

}
