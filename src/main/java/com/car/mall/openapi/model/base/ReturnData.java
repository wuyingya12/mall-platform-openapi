package com.car.mall.openapi.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("返回信息")
public class ReturnData<T> {
    @ApiModelProperty(required = true,value ="返回状态码(1成功，-1失败)")
    private int code;
    @ApiModelProperty(required = true,value ="状态码描述")
    private String message;
    @ApiModelProperty(required = false, value = "响应的业务数据")
    private T data;

    public static <T> ReturnData<T> error(String message) {
        ReturnData<T> entity = new ReturnData();
        entity.setCode(-1);
        entity.setMessage(message);
        return entity;
    }

    public static <T> ReturnData<T> success(String message, T data) {
        ReturnData<T> entity = new ReturnData();
        entity.setCode(1);
        entity.setMessage(message);
        entity.setData(data);
        return entity;
    }

    public static <T> ReturnData<T> success(String message) {
        ReturnData<T> entity = new ReturnData();
        entity.setCode(1);
        entity.setMessage(message);
        return entity;
    }

    public static <T> ReturnData<T> success(T data) {
        ReturnData<T> entity = new ReturnData();
        entity.setCode(1);
        entity.setData(data);
        return entity;
    }

    public ReturnData(){
        code = 1;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
