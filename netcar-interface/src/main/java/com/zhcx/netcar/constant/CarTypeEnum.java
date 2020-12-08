package com.zhcx.netcar.constant;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/4/10 0010 10:46
 **/
public enum CarTypeEnum {

    /**
     * 营运汽车类型 0-不区分 1-出租车 2-网约车
     */
    ALL(0, "all"),
    TAXI(1, "taxi"),
    NETCAR(2, "netcar");

    private final int code;
    private final String desc;


    CarTypeEnum(int code, String desc) {
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
