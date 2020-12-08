package com.zhcx.netcar.pojo.base;

import java.io.Serializable;

public class NetcarPositionDriver extends NetcarBasePojo implements Serializable {

    private static final long serialVersionUID = -4197377602364374055L;
    private String companyId;

    private String licenseId;

    private Integer driverRegionCode;

    private String vehicleNo;

    private String positionTime;

    private Double longitude;

    private Double latitude;

    private Integer encrypt;

    private Double direction;

    private Double elevation;

    private Double speed;

    private Integer bizStatus;

    private String orderId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public Integer getDriverRegionCode() {
        return driverRegionCode;
    }

    public void setDriverRegionCode(Integer driverRegionCode) {
        this.driverRegionCode = driverRegionCode;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
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

    public Integer getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(Integer encrypt) {
        this.encrypt = encrypt;
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

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
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

    @Override
    public String toString() {
        return "NetcarPositionDriver{" +
                "companyId='" + companyId + '\'' +
                ", licenseId='" + licenseId + '\'' +
                ", driverRegionCode=" + driverRegionCode +
                ", vehicleNo='" + vehicleNo + '\'' +
                ", positionTime='" + positionTime + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", encrypt=" + encrypt +
                ", direction=" + direction +
                ", elevation=" + elevation +
                ", speed=" + speed +
                ", bizStatus=" + bizStatus +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}