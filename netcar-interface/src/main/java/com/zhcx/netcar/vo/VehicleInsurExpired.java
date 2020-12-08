package com.zhcx.netcar.vo;

import com.zhcx.netcar.annotation.FieldMeta;

import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/7/18 16:52
 * 保险到期
 **/
public class VehicleInsurExpired implements Serializable {

    private static final long serialVersionUID = -553749240104858408L;
    @FieldMeta(name = "车牌", description = "车牌", index = 0)
    private String vehicleNo;

    @FieldMeta(name = "保险类型", description = "保险类型", index = 1)
    private String insurType;

    @FieldMeta(name = "保险金额", description = "保险金额", index = 2)
    private String insurCount;

    @FieldMeta(name = "保险到期时间", description = "保险到期时间", index = 3)
    private String insurExp;

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getInsurType() {
        return insurType;
    }

    public void setInsurType(String insurType) {
        this.insurType = insurType;
    }

    public String getInsurCount() {
        return insurCount;
    }

    public void setInsurCount(String insurCount) {
        this.insurCount = insurCount;
    }

    public String getInsurExp() {
        return insurExp;
    }

    public void setInsurExp(String insurExp) {
        this.insurExp = insurExp;
    }
}
