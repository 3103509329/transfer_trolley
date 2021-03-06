package com.zhcx.netcar.vo;

import com.zhcx.netcar.annotation.FieldMeta;

import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/7/18 16:52
 * 年龄过大
 **/
public class DriverAgeBeyond implements Serializable {

    private static final long serialVersionUID = -553749240104858408L;

    @FieldMeta(name = "驾驶员姓名", description = "驾驶员姓名", index = 0)
    private String driverName;

    @FieldMeta(name = "电话", description = "电话", index = 1)
    private String driverPhone;

    @FieldMeta(name = "驾驶证号码", description = "驾驶证号码", index = 2)
    private String licenseId;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

}
