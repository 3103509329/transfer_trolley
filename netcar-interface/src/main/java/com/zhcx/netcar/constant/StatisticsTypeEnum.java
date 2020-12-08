package com.zhcx.netcar.constant;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/5/9 0009 20:00
 **/
public enum StatisticsTypeEnum {

    MONEY_TOTAL(1,"营运金额统计"),
    ORDER_TOTAL(2,"订单统计"),
    DRIVER_TOTAL(3,"车辆统计");


    private final int code;

    private final String desc;

    StatisticsTypeEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
