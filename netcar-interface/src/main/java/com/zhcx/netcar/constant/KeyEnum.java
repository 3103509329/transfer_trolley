package com.zhcx.netcar.constant;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/10 18:20
 * cassandra entity_type 类型
 **/
public enum KeyEnum {

    /**
     * 关键字类型
     */
    FULL("FULL", "全部"),
    GPS_MESSAGE("GPS_MESSAGE", "GPS信息");

    private final String code;

    private final String desc;

    KeyEnum(String code, String desc){
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
