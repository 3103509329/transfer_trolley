package com.zhcx.netcar.constant;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/7/2 0002 10:37
 **/
public enum CompanyEnum {
    /**
     * Excel文件列标题
     */
    COMPANY_ID(0,"公司标识"),
    COMPANY_NAME(1,"公司名称"),
    IDENTIFIER(2,"统一社会信用代码"),
    ADDRESS(3,"注册地行政区划代码"),
    BUSINESS_SCOPE(4,"经营范围"),
    CONTACT_ADDRESS(5,"通信地址"),
    ECONOMIC_TYPE(6,"经营业户经济类型"),
    REG_CAPITAL(7,"注册资本"),
    LEGAL_NAME(8,"法人代表姓名"),
    LEGAL_PHONE(9,"法人代表电话"),
    LEGAL_ID(10,"法人代表身份证号"),
    LEGAL_PHOTO(11,"法人代表身份证扫描件文件编号"),
    STATE(12,"状态"),
    FLAG(13,"操作标识"),
    UPDATE_TIME(14,"更新时间");

    private final int code;

    private final String desc;

    CompanyEnum(int code, String desc) {
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
