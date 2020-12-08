package com.zhcx.netcar.constant;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/7/2 0002 15:58
 **/
public enum  DriverEnum {

    COMPANY_ID(0,"公司标识"),
    ADDRESS(1,"注册地址"),
    DRIVER_NAME(2,"姓名"),
    DRIVER_PHONE(3,"手机号"),
    DRIVER_GENDER(4,"性别"),
    DRIVER_BIRTHDAY(5,"生日"),
    DRIVER_NATIONALITY(6,"国籍"),
    DRIVER_NATION(7,"民族"),
    DRIVER_MARITAL_STATUS(8,"婚姻状况"),
    DRIVER_LANGUAGE_LEVEL(9,"外语能力"),
    DRIVER_EDUCATION(10,"驾驶员学历"),
    DRIVER_CENSUS(11,"户口登记机关名称"),
    DRIVER_ADDRESS(12,"户口住址或长住地址"),
    DRIVER_CONTACT_ADDRESS(13,"通信地址"),
    PHOTO_ID(14,"驾驶员照片文件编号"),
    LICENSE_ID(15,"机动车驾驶证号"),
    LICENSE_PHOTO_ID(16,"机动车驾驶证扫描件文件编号"),
    DRIVER_TYPE(17,"准驾车型"),
    GET_DRIVER_LICENSE_DATE(18,"初次领取驾驶证日期"),
    DRIVER_LICENSE_OFF(19,"驾驶证有效期限止"),
    DRIVER_LICENSE_ON(20,"驾驶证有效期限起"),
    TAXI_DRIVER(21,"是否巡游出租汽车驾驶员"),
    CERTIFICATE_NO(22,"网络预约出租汽车驾驶员资格证号"),
    NETWORK_CAR_ISSUE_ORGANIZATION(23,"网络预约出租汽车驾驶员证发证机构"),
    NETWORK_CAR_ISSUE_DATE(24,"资格证发证曰期"),
    GET_NETWORK_CAR_PROOF_DATE(25,"初次领取资格证日期"),
    NETWORK_CAR_PROOF_ON(26,"资格证有效起始曰期"),
    NETWORK_CAR_PROOF_OFF(27,"资格证有效截止曰期"),
    REGISTER_DATE(28,"报备日期"),
    FULL_TIME_DRIVER(29,"是否专职驾驶员"),
    IN_DRIVER_BLACKLIST(30,"是否在驾驶员黑名单内"),
    COMMERCIAL_TYPE(31,"服务类型"),
    CONTRACT_COMPANY(32,"驾驶员合同（或协议） 签署公司"),
    CONTRACT_ON(33,"合同（或协议）有效期起"),
    CONTRACT_OFF(34,"合同（或协议）有效期止"),
    EMERGENCY_CONTACT(35,"紧急情况联系人"),
    EMERGENCY_CONTACT_PHONE(36,"紧急情况联系人电话，手机号"),
    EMERGENCY_CONTACT_ADDRESS(37,"紧急情况联系人通信地址"),
    STATE(38,"状态"),
    FLAG(39,"操作标识"),
    UPDATE_TIME(40,"更新时间");

    private final int code;

    private final String desc;

    DriverEnum(int code, String desc) {
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
