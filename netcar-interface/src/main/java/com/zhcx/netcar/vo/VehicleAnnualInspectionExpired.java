package com.zhcx.netcar.vo;

import com.zhcx.netcar.annotation.FieldMeta;

import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/7/18 16:52
 * 年检过期
 **/
public class VehicleAnnualInspectionExpired implements Serializable {

    private static final long serialVersionUID = -553749240104858408L;
    @FieldMeta(name = "车牌", description = "车牌", index = 0)
    private String vehicleNo;

    @FieldMeta(name = "车辆下次年检时间", description = "车辆下次年检时间", index = 1)
    private String nextFixDate;

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getNextFixDate() {
        return nextFixDate;
    }

    public void setNextFixDate(String nextFixDate) {
        this.nextFixDate = nextFixDate;
    }
}
