package com.zhcx.netcar.constant;

public enum OrderType {
    CRATE_ORDER("0", "订单发起"),
    SUCCESS_ORDER("1", "订单成功"),
    CANCEL_ORDER("2", "失败");

    private final String code;

    private final String desc;

    OrderType(String code, String desc){
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
