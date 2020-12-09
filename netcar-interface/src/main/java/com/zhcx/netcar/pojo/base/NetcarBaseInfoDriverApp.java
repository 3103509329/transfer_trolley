package com.zhcx.netcar.pojo.base;

import java.io.Serializable;

public class NetcarBaseInfoDriverApp extends NetcarBasePojo implements Serializable {
    private String companyId;

    private String licenseId;

    private Integer address;

    private String driverPhone;

    private Integer netType;

    private String appVersion;

    private Integer mapType;

    private Integer state;

    private Integer flag;

    private String updateTime;

    private String reserved;

    private static final long serialVersionUID = 1L;

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

    public Integer getAddress() {
        return address;
    }

    public void setAddress(Integer address) {
        this.address = address;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public Integer getNetType() {
        return netType;
    }

    public void setNetType(Integer netType) {
        this.netType = netType;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Integer getMapType() {
        return mapType;
    }

    public void setMapType(Integer mapType) {
        this.mapType = mapType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
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
        NetcarBaseInfoDriverApp other = (NetcarBaseInfoDriverApp) that;
        return (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getLicenseId() == null ? other.getLicenseId() == null : this.getLicenseId().equals(other.getLicenseId()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getDriverPhone() == null ? other.getDriverPhone() == null : this.getDriverPhone().equals(other.getDriverPhone()))
            && (this.getNetType() == null ? other.getNetType() == null : this.getNetType().equals(other.getNetType()))
            && (this.getAppVersion() == null ? other.getAppVersion() == null : this.getAppVersion().equals(other.getAppVersion()))
            && (this.getMapType() == null ? other.getMapType() == null : this.getMapType().equals(other.getMapType()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
            && (this.getFlag() == null ? other.getFlag() == null : this.getFlag().equals(other.getFlag()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getReserved() == null ? other.getReserved() == null : this.getReserved().equals(other.getReserved()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getLicenseId() == null) ? 0 : getLicenseId().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getDriverPhone() == null) ? 0 : getDriverPhone().hashCode());
        result = prime * result + ((getNetType() == null) ? 0 : getNetType().hashCode());
        result = prime * result + ((getAppVersion() == null) ? 0 : getAppVersion().hashCode());
        result = prime * result + ((getMapType() == null) ? 0 : getMapType().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        result = prime * result + ((getFlag() == null) ? 0 : getFlag().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
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
        sb.append(", licenseId=").append(licenseId);
        sb.append(", address=").append(address);
        sb.append(", driverPhone=").append(driverPhone);
        sb.append(", netType=").append(netType);
        sb.append(", appVersion=").append(appVersion);
        sb.append(", mapType=").append(mapType);
        sb.append(", state=").append(state);
        sb.append(", flag=").append(flag);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", reserved=").append(reserved);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}