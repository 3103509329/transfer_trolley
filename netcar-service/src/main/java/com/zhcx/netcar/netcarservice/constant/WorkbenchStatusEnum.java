package com.zhcx.netcar.netcarservice.constant;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/9/6 9:25
 * 数据状态 类型 1-部平台车辆数据缺失 2-已取证未运营车辆数 3-车辆里程超限车辆数 4-保险到期车辆数 5-保险金额不合格
 * 6-无车载终端 7-年检过期的车辆数 8-驾照过期 9-年龄超大 10-持证未上岗 11-部平台驾驶员信息缺失
 **/
public enum WorkbenchStatusEnum {

    /**
     * 状态
     */
    LACK_VEHICLE_DATA(1, "部平台车辆数据缺失"),
    NOT_OPERATING(2, "已取证未运营车辆数"),
    MILEAGE_TRANSFINITE(3, "车辆里程超限车辆数"),
    INSURANCE_DUE(4, "保险到期车辆数"),
    INSURED_AMOUNT_UNQUALIFIED(5, "保险金额不合格"),
    NOT_TERMINAL(6, "无车载终端"),
    ANNUAL_INSPECTION_OVERDUE(7, "年检过期的车辆数"),
    LICENSE_OVERDUE(8, "驾照过期"),
    AGE_EXCESSIVE(9, "年龄超大"),
    NOT_MOUNT_GUARD(10, "持证未上岗"),
    LACK_DRIVER_DATA(11, "部平台驾驶员信息缺失");

    private final int code;
    private final String desc;


    WorkbenchStatusEnum(int code, String desc) {
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
