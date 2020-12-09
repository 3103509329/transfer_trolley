package com.zhcx.netcar.pojo.base;

import java.io.Serializable;

public class NetcarOperateDepart extends NetcarBasePojo implements Serializable {
    private String companyId;

    private String orderId;

    private String licenseId;

    private String fareType;

    private String vehicleNo;

    private Double depLongitude;

    private Double depLatitude;

    private Integer encrypt;

    private String depTime;

    private Double waitMile;

    private Integer waitTime;

    private String reserved;

    private static final long serialVersionUID = 1L;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getFareType() {
        return fareType;
    }

    public void setFareType(String fareType) {
        this.fareType = fareType;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public Double getDepLongitude() {
        return depLongitude;
    }

    public void setDepLongitude(Double depLongitude) {
        this.depLongitude = depLongitude;
    }

    public Double getDepLatitude() {
        return depLatitude;
    }

    public void setDepLatitude(Double depLatitude) {
        this.depLatitude = depLatitude;
    }

    public Integer getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(Integer encrypt) {
        this.encrypt = encrypt;
    }

    public String getDepTime() {
        return depTime;
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
    }

    public Double getWaitMile() {
        return waitMile;
    }

    public void setWaitMile(Double waitMile) {
        this.waitMile = waitMile;
    }

    public Integer getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Integer waitTime) {
        this.waitTime = waitTime;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        NetcarOperateDepart other = (NetcarOperateDepart) that;
        return (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getLicenseId() == null ? other.getLicenseId() == null : this.getLicenseId().equals(other.getLicenseId()))
            && (this.getFareType() == null ? other.getFareType() == null : this.getFareType().equals(other.getFareType()))
            && (this.getVehicleNo() == null ? other.getVehicleNo() == null : this.getVehicleNo().equals(other.getVehicleNo()))
            && (this.getDepLongitude() == null ? other.getDepLongitude() == null : this.getDepLongitude().equals(other.getDepLongitude()))
            && (this.getDepLatitude() == null ? other.getDepLatitude() == null : this.getDepLatitude().equals(other.getDepLatitude()))
            && (this.getEncrypt() == null ? other.getEncrypt() == null : this.getEncrypt().equals(other.getEncrypt()))
            && (this.getDepTime() == null ? other.getDepTime() == null : this.getDepTime().equals(other.getDepTime()))
            && (this.getWaitMile() == null ? other.getWaitMile() == null : this.getWaitMile().equals(other.getWaitMile()))
            && (this.getWaitTime() == null ? other.getWaitTime() == null : this.getWaitTime().equals(other.getWaitTime()))
            && (this.getReserved() == null ? other.getReserved() == null : this.getReserved().equals(other.getReserved()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getLicenseId() == null) ? 0 : getLicenseId().hashCode());
        result = prime * result + ((getFareType() == null) ? 0 : getFareType().hashCode());
        result = prime * result + ((getVehicleNo() == null) ? 0 : getVehicleNo().hashCode());
        result = prime * result + ((getDepLongitude() == null) ? 0 : getDepLongitude().hashCode());
        result = prime * result + ((getDepLatitude() == null) ? 0 : getDepLatitude().hashCode());
        result = prime * result + ((getEncrypt() == null) ? 0 : getEncrypt().hashCode());
        result = prime * result + ((getDepTime() == null) ? 0 : getDepTime().hashCode());
        result = prime * result + ((getWaitMile() == null) ? 0 : getWaitMile().hashCode());
        result = prime * result + ((getWaitTime() == null) ? 0 : getWaitTime().hashCode());
        result = prime * result + ((getReserved() == null) ? 0 : getReserved().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", companyId=").append(companyId);
        sb.append(", orderId=").append(orderId);
        sb.append(", licenseId=").append(licenseId);
        sb.append(", fareType=").append(fareType);
        sb.append(", vehicleNo=").append(vehicleNo);
        sb.append(", depLongitude=").append(depLongitude);
        sb.append(", depLatitude=").append(depLatitude);
        sb.append(", encrypt=").append(encrypt);
        sb.append(", depTime=").append(depTime);
        sb.append(", waitMile=").append(waitMile);
        sb.append(", waitTime=").append(waitTime);
        sb.append(", reserved=").append(reserved);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}