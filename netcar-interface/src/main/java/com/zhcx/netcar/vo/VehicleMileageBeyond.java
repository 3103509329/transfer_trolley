package com.zhcx.netcar.vo;

import com.zhcx.netcar.annotation.FieldMeta;

import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/7/18 16:52
 * 里程超限车辆信息
 **/
public class VehicleMileageBeyond implements Serializable {

    private static final long serialVersionUID = -553749240104858408L;
    @FieldMeta(name = "车牌", description = "车牌", index = 0)
    private String vehicleNo;

    @FieldMeta(name = "行驶总里程", description = "行驶总里程", index = 1)
    private String totalMile;

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getTotalMile() {
        return totalMile;
    }

    public void setTotalMile(String totalMile) {
        this.totalMile = totalMile;
    }
}
