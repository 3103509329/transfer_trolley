package com.zhcx.netcar.pojo.base;

import java.io.Serializable;

public class NetcarBaseInfoCompanyStat extends NetcarBasePojo implements Serializable {
    private String companyId;

    private Long vehicleNum;

    private Long driverNum;

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

    public Long getVehicleNum() {
        return vehicleNum;
    }

    public void setVehicleNum(Long vehicleNum) {
        this.vehicleNum = vehicleNum;
    }

    public Long getDriverNum() {
        return driverNum;
    }

    public void setDriverNum(Long driverNum) {
        this.driverNum = driverNum;
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
        NetcarBaseInfoCompanyStat other = (NetcarBaseInfoCompanyStat) that;
        return (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getVehicleNum() == null ? other.getVehicleNum() == null : this.getVehicleNum().equals(other.getVehicleNum()))
            && (this.getDriverNum() == null ? other.getDriverNum() == null : this.getDriverNum().equals(other.getDriverNum()))
            && (this.getFlag() == null ? other.getFlag() == null : this.getFlag().equals(other.getFlag()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getReserved() == null ? other.getReserved() == null : this.getReserved().equals(other.getReserved()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getVehicleNum() == null) ? 0 : getVehicleNum().hashCode());
        result = prime * result + ((getDriverNum() == null) ? 0 : getDriverNum().hashCode());
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
        sb.append(", vehicleNum=").append(vehicleNum);
        sb.append(", driverNum=").append(driverNum);
        sb.append(", flag=").append(flag);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", reserved=").append(reserved);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}