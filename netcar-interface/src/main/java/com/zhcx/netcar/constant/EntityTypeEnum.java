package com.zhcx.netcar.constant;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/10 18:20
 * cassandra entity_type 类型
 **/
public enum EntityTypeEnum {

    /**
     * 实体类型
     */
    DRIVER("DRIVER", "驾驶员"),
    VEHICLE("VEHICLE", "车辆");

    private final String code;

    private final String desc;

    EntityTypeEnum(String code, String desc){
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
