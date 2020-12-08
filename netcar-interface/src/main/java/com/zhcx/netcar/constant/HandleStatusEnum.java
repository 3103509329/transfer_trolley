package com.zhcx.netcar.constant;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/9/6 9:25
 * 报警状态
 **/
public enum HandleStatusEnum {

    /**
     * 状态
     */
    UN_HANDLE(0, "待处理"),
    HANDLING(1, "处理中");

    private final int code;
    private final String desc;


    HandleStatusEnum(int code, String desc) {
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
