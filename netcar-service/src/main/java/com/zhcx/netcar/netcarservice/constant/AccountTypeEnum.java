package com.zhcx.netcar.netcarservice.constant;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/9/6 9:25
 * 账号类型状态
 **/
public enum AccountTypeEnum {

    /**
     * 状态 0-个人 1-企业 -1-其他
     */
    DRIVER("0", "个人"),
    COMPANY("1", "企业"),
    OTHER("2", "其他");

    private final String code;
    private final String desc;


    AccountTypeEnum(String code, String desc) {
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
