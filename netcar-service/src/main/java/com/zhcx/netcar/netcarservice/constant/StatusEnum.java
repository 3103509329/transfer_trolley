package com.zhcx.netcar.netcarservice.constant;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/9/6 9:25
 * 数据状态
 **/
public enum StatusEnum {

    /**
     * 状态
     */
    DISABLE(0, "无效"),
    ENABLE(1, "有效"),
    DELETE(2, "删除"),
    OTHER(3, "其他");

    private final int code;
    private final String desc;


    StatusEnum(int code, String desc) {
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
