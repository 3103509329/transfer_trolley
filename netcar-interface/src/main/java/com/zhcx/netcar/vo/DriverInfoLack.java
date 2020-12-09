package com.zhcx.netcar.vo;

import com.zhcx.netcar.annotation.FieldMeta;

import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/7/18 16:52
 * 部平台驾驶员信息缺失
 **/
public class DriverInfoLack implements Serializable {

    private static final long serialVersionUID = 1599818513859172859L;

    @FieldMeta(name = "驾驶员姓名", description = "驾驶员姓名", index = 0)
    private String name;

    @FieldMeta(name = "身份证号", description = "身份证号", index = 1)
    private String cardno;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }
}
