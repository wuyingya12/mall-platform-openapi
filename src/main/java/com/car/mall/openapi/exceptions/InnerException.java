package com.car.mall.openapi.exceptions;

/**
 * 内部异常（自定义）.
 */
public class InnerException extends RuntimeException {
    private String messageData;

    private Exception exception;

    public InnerException(String messageData,Exception exception){
        this.messageData = messageData;
        this.exception = exception;
    }


    public String getMessageData() {
        return messageData;
    }

    public void setMessageData(String messageData) {
        this.messageData = messageData;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
