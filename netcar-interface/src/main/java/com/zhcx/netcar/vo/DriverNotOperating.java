package com.zhcx.netcar.vo;

import com.zhcx.netcar.annotation.FieldMeta;

import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/7/18 16:52
 * 未经营上岗
 **/
public class DriverNotOperating implements Serializable {

    private static final long serialVersionUID = -553749240104858408L;

    @FieldMeta(name = "驾驶员姓名", description = "驾驶员姓名", index = 0)
    private String name;

    @FieldMeta(name = "电话", description = "电话", index = 1)
    private String driverPhone;

    @FieldMeta(name = "身份证号", description = "身份证号", index = 2)
    private String cardno;

    @FieldMeta(name = "网络预约出租汽车驾驶员资格证号", description = "网络预约出租汽车驾驶员资格证号", index = 3)
    private String certificateNo;

    @FieldMeta(name = "发证机构", description = "发证机构", index = 4)
    private String networkCarIssueOrganization;

    @FieldMeta(name = "发证日期", description = "发证日期", index = 5)
    private String networkCarIssueDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getNetworkCarIssueOrganization() {
        return networkCarIssueOrganization;
    }

    public void setNetworkCarIssueOrganization(String networkCarIssueOrganization) {
        this.networkCarIssueOrganization = networkCarIssueOrganization;
    }

    public String getNetworkCarIssueDate() {
        return networkCarIssueDate;
    }

    public void setNetworkCarIssueDate(String networkCarIssueDate) {
        this.networkCarIssueDate = networkCarIssueDate;
    }
}
