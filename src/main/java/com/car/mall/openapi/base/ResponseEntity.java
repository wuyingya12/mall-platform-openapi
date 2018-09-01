package com.car.mall.openapi.base;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author dongliu
 * @version 1.0
 * file was created in 2018/5/4.
 */
public class ResponseEntity<T> implements Serializable {
    private static final int SUCCESS = 200;
    private static final String SUCCESS_MESSAGE = "操作成功";
    private static final int ERROR = 500;
    private static final String ERROR_MESSAGE = "操作失败";
    private static final int UNAUTH = 401;
    private static final String UNAUTH_MESSAGE = "无操作权限";
    private static final int SESSIONOVER = 410;
    private static final String SESSIONOVER_MESSAGE = "请进行登录";
    @ApiModelProperty(required = true, value = "响应时的时间戳")
    private long curtime = System.currentTimeMillis();
    @ApiModelProperty(required = true, value = "响应的错误代码")
    private Integer errorCode;
    @ApiModelProperty(required = true, value = "响应的消息")
    private String message;
    @ApiModelProperty(required = false, value = "响应的token")
    private String token;
    @ApiModelProperty(required = false, value = "响应的业务数据")
    private T data;

    public static <T> ResponseEntity<T> success() {
        ResponseEntity<T> entity = new ResponseEntity<T>();
        entity.setErrorCode(SUCCESS);
        entity.setMessage(SUCCESS_MESSAGE);
        return entity;
    }

    public static <T> ResponseEntity<T> success(String message) {
        ResponseEntity<T> entity = success();
        entity.setMessage(message);
        return entity;
    }

    public static <T> ResponseEntity<T> success(T data) {
        ResponseEntity<T> entity = success();
        entity.setData(data);
        return entity;
    }

    public static <T> ResponseEntity<T> success(T data, String token) {
        ResponseEntity<T> entity = success();
        entity.setToken(token);
        entity.setData(data);
        return entity;
    }

    public static <T> ResponseEntity<T> error() {
        ResponseEntity<T> entity = new ResponseEntity<T>();
        entity.setErrorCode(ERROR);
        entity.setMessage(ERROR_MESSAGE);
        return entity;
    }

    public static <T> ResponseEntity<T> error(Integer errorCode) {
        ResponseEntity<T> entity = error();
        entity.setErrorCode(errorCode);
        return entity;
    }

    public static <T> ResponseEntity<T> error(String message) {
        ResponseEntity<T> entity = error();
        entity.setMessage(message);
        return entity;
    }

    public static <T> ResponseEntity<T> error(Integer errorCode, String message) {
        ResponseEntity<T> entity = error();
        entity.setErrorCode(errorCode);
        entity.setMessage(message);
        return entity;
    }

    public static <T> ResponseEntity<T> unauth() {
        ResponseEntity<T> entity = new ResponseEntity<T>();
        entity.setErrorCode(UNAUTH);
        entity.setMessage(UNAUTH_MESSAGE);
        return entity;
    }

    public static <T> ResponseEntity<T> unauth(String message) {
        ResponseEntity<T> entity = unauth();
        entity.setMessage(message);
        return entity;
    }

    public static <T> ResponseEntity<T> sessionOver() {
        ResponseEntity<T> entity = new ResponseEntity<T>();
        entity.setErrorCode(SESSIONOVER);
        entity.setMessage(SESSIONOVER_MESSAGE);
        return entity;
    }

    public static <T> ResponseEntity<T> sessionOver(String message) {
        ResponseEntity<T> entity = sessionOver();
        entity.setErrorCode(SESSIONOVER);
        entity.setMessage(message);
        return entity;
    }


    public long getCurtime() {
        return curtime;
    }

    public void setCurtime(long curtime) {
        this.curtime = curtime;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
