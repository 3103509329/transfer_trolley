package com.zhcx.authorization.utils;

import java.io.Serializable;

public class RespMessage implements Serializable {

    private Object msgData;

    private String code;

    private String result=Boolean.TRUE.toString();

    public RespMessage(){}

    public RespMessage(Object msgData) {
        this.msgData = msgData;
        this.code = "200";
        this.result = Boolean.TRUE.toString();
    }

    public RespMessage(Object msgData, String code) {
        this.msgData = msgData;
        this.code = code;
        this.result = Boolean.FALSE.toString();
    }

    public RespMessage(Object msgData, String code, String result) {
        this.msgData = msgData;
        this.code = code;
        this.result = result;
    }

    public Object getMsgData() {
        return msgData;
    }

    public void setMsgData(Object msgData) {
        this.msgData = msgData;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
