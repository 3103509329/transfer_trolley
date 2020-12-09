package com.zhcx.netcar.pojo.base;

import java.io.Serializable;

public class NetcarBaseInfoDriverStat extends NetcarBasePojo implements Serializable {
    private String companyId;

    private String licenseId;

    private Integer address;

    private String cycle;

    private Integer orderCount;

    private Integer trafficViolationCount;

    private Integer complainedCount;

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

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getTrafficViolationCount() {
        return trafficViolationCount;
    }

    public void setTrafficViolationCount(Integer trafficViolationCount) {
        this.trafficViolationCount = trafficViolationCount;
    }

    public Integer getComplainedCount() {
        return complainedCount;
    }

    public void setComplainedCount(Integer complainedCount) {
        this.complainedCount = complainedCount;
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
        NetcarBaseInfoDriverStat other = (NetcarBaseInfoDriverStat) that;
        return (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getLicenseId() == null ? other.getLicenseId() == null : this.getLicenseId().equals(other.getLicenseId()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getCycle() == null ? other.getCycle() == null : this.getCycle().equals(other.getCycle()))
            && (this.getOrderCount() == null ? other.getOrderCount() == null : this.getOrderCount().equals(other.getOrderCount()))
            && (this.getTrafficViolationCount() == null ? other.getTrafficViolationCount() == null : this.getTrafficViolationCount().equals(other.getTrafficViolationCount()))
            && (this.getComplainedCount() == null ? other.getComplainedCount() == null : this.getComplainedCount().equals(other.getComplainedCount()))
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
        result = prime * result + ((getCycle() == null) ? 0 : getCycle().hashCode());
        result = prime * result + ((getOrderCount() == null) ? 0 : getOrderCount().hashCode());
        result = prime * result + ((getTrafficViolationCount() == null) ? 0 : getTrafficViolationCount().hashCode());
        result = prime * result + ((getComplainedCount() == null) ? 0 : getComplainedCount().hashCode());
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
        sb.append(", cycle=").append(cycle);
        sb.append(", orderCount=").append(orderCount);
        sb.append(", trafficViolationCount=").append(trafficViolationCount);
        sb.append(", complainedCount=").append(complainedCount);
        sb.append(", flag=").append(flag);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", reserved=").append(reserved);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}