package com.zhcx.netcar.params;

import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/8 15:00
 **/
public class PassengerComplainParam extends BaseParam implements Serializable {
    private static final long serialVersionUID = 3382929478143470256L;

    /**
     * 订单号
     */
    private String orderId;

    private String vehicleNo;

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
