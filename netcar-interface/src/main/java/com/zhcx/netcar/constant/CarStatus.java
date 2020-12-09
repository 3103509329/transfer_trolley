package com.zhcx.netcar.constant;

public enum  CarStatus {

    CAR_PASSENGER("1", "载客"),
    CAR_ORDER("2", "接单"),
    CAR_NULL("3", "空驶"),
    CAR_OUT_SERVICE("4", "停运");
    private final String code;

    private final String desc;

    CarStatus(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
