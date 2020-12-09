package com.zhcx.netcar.netcarservice.constant;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/9/6 9:25
 * 审核状态
 **/
public enum VerifyStatusEnum {

    /**
     * 状态 0-待审核 1-已通过 -1-未通过
     */
    WAIT(0, "待审核"),
    PASS(1, "已通过"),
    REFUSE(-1, "未通过");

    private final int code;
    private final String desc;


    VerifyStatusEnum(int code, String desc) {
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
