package com.zhcx.netcar.constant;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/4/10 0010 10:21
 **/
public enum CompanyDataTypeEnum {


    /**
     * redis Key值前缀（企业数据同步用）
     */
    NETCAR_COMPANY(0, "netcar_company:"),
    TAXI_COMPANY(1, "taxi_company:");

    private final int code;
    private final String desc;


    CompanyDataTypeEnum(int code, String desc) {
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
