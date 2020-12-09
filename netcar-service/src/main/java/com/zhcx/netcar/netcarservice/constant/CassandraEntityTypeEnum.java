package com.zhcx.netcar.netcarservice.constant;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/9/6 9:25
 * 账号类型状态
 **/
public enum CassandraEntityTypeEnum {

    /**
     * 状态 0-个人 1-企业 -1-其他
     */
    DRIVER("DRIVER", "驾驶员"),
    VEHICLE("VEHICLE", "车辆");

    private final String code;
    private final String desc;


    CassandraEntityTypeEnum(String code, String desc) {
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
