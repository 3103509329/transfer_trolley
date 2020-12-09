package com.zhcx.netcar.pojo.base;

import java.io.Serializable;

public class NetcarOperateArrive extends NetcarBasePojo implements Serializable {
    private String companyId;

    private String orderId;

    private Double destLongitude;

    private Double destLatitude;

    private Integer encrypt;

    private String destTime;

    private Double driveMile;

    private Integer driveTime;

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

    public Double getDestLongitude() {
        return destLongitude;
    }

    public void setDestLongitude(Double destLongitude) {
        this.destLongitude = destLongitude;
    }

    public Double getDestLatitude() {
        return destLatitude;
    }

    public void setDestLatitude(Double destLatitude) {
        this.destLatitude = destLatitude;
    }

    public Integer getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(Integer encrypt) {
        this.encrypt = encrypt;
    }

    public String getDestTime() {
        return destTime;
    }

    public void setDestTime(String destTime) {
        this.destTime = destTime;
    }

    public Double getDriveMile() {
        return driveMile;
    }

    public void setDriveMile(Double driveMile) {
        this.driveMile = driveMile;
    }

    public Integer getDriveTime() {
        return driveTime;
    }

    public void setDriveTime(Integer driveTime) {
        this.driveTime = driveTime;
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
        NetcarOperateArrive other = (NetcarOperateArrive) that;
        return (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getDestLongitude() == null ? other.getDestLongitude() == null : this.getDestLongitude().equals(other.getDestLongitude()))
            && (this.getDestLatitude() == null ? other.getDestLatitude() == null : this.getDestLatitude().equals(other.getDestLatitude()))
            && (this.getEncrypt() == null ? other.getEncrypt() == null : this.getEncrypt().equals(other.getEncrypt()))
            && (this.getDestTime() == null ? other.getDestTime() == null : this.getDestTime().equals(other.getDestTime()))
            && (this.getDriveMile() == null ? other.getDriveMile() == null : this.getDriveMile().equals(other.getDriveMile()))
            && (this.getDriveTime() == null ? other.getDriveTime() == null : this.getDriveTime().equals(other.getDriveTime()))
            && (this.getReserved() == null ? other.getReserved() == null : this.getReserved().equals(other.getReserved()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getDestLongitude() == null) ? 0 : getDestLongitude().hashCode());
        result = prime * result + ((getDestLatitude() == null) ? 0 : getDestLatitude().hashCode());
        result = prime * result + ((getEncrypt() == null) ? 0 : getEncrypt().hashCode());
        result = prime * result + ((getDestTime() == null) ? 0 : getDestTime().hashCode());
        result = prime * result + ((getDriveMile() == null) ? 0 : getDriveMile().hashCode());
        result = prime * result + ((getDriveTime() == null) ? 0 : getDriveTime().hashCode());
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
        sb.append(", destLongitude=").append(destLongitude);
        sb.append(", destLatitude=").append(destLatitude);
        sb.append(", encrypt=").append(encrypt);
        sb.append(", destTime=").append(destTime);
        sb.append(", driveMile=").append(driveMile);
        sb.append(", driveTime=").append(driveTime);
        sb.append(", reserved=").append(reserved);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}