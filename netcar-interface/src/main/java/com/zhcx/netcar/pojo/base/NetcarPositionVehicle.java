package com.zhcx.netcar.pojo.base;

import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/18 16:17
 * 车辆定位
 **/
public class NetcarPositionVehicle extends NetcarBasePojo implements Serializable {

    private static final long serialVersionUID = 6871604562768593923L;
    private String companyId;

    private String vehicleNo;

    private Integer vehicleRegionCode;

    private String positionTime;

    private Double longitude;

    private Double latitude;

    private Double speed;

    private Double direction;

    private Double elevation;

    private Double mileage;

    private Integer encrypt;

    private Integer warnStatus;

    private Integer vehStatus;

    private Integer bizStatus;

    private String orderId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public Integer getVehicleRegionCode() {
        return vehicleRegionCode;
    }

    public void setVehicleRegionCode(Integer vehicleRegionCode) {
        this.vehicleRegionCode = vehicleRegionCode;
    }

    public String getPositionTime() {
        return positionTime;
    }

    public void setPositionTime(String positionTime) {
        this.positionTime = positionTime;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getDirection() {
        return direction;
    }

    public void setDirection(Double direction) {
        this.direction = direction;
    }

    public Double getElevation() {
        return elevation;
    }

    public void setElevation(Double elevation) {
        this.elevation = elevation;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public Integer getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(Integer encrypt) {
        this.encrypt = encrypt;
    }

    public Integer getWarnStatus() {
        return warnStatus;
    }

    public void setWarnStatus(Integer warnStatus) {
        this.warnStatus = warnStatus;
    }

    public Integer getVehStatus() {
        return vehStatus;
    }

    public void setVehStatus(Integer vehStatus) {
        this.vehStatus = vehStatus;
    }

    public Integer getBizStatus() {
        return bizStatus;
    }

    public void setBizStatus(Integer bizStatus) {
        this.bizStatus = bizStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
