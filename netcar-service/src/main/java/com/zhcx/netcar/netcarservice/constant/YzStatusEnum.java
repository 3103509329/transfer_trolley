package com.zhcx.netcar.netcarservice.constant;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/9/6 9:25
 * 数据状态
 **/
public enum YzStatusEnum {

    /**
     * 状态
     */
    ADD(1, "新增"),
    UPDATE(2, "更新"),
    DELETE(3, "删除");

    private final int code;
    private final String desc;


    YzStatusEnum(int code, String desc) {
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
