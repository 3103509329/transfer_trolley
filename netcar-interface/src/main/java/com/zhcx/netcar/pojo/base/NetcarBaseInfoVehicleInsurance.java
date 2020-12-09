package com.zhcx.netcar.pojo.base;

import java.io.Serializable;

public class NetcarBaseInfoVehicleInsurance extends NetcarBasePojo implements Serializable {
    private String companyId;

    private String vehicleNo;

    private String insurCom;

    private String insurNum;

    private String insurType;

    private Double insurCount;

    private String insurEff;

    private String insurExp;

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

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getInsurCom() {
        return insurCom;
    }

    public void setInsurCom(String insurCom) {
        this.insurCom = insurCom;
    }

    public String getInsurNum() {
        return insurNum;
    }

    public void setInsurNum(String insurNum) {
        this.insurNum = insurNum;
    }

    public String getInsurType() {
        return insurType;
    }

    public void setInsurType(String insurType) {
        this.insurType = insurType;
    }

    public Double getInsurCount() {
        return insurCount;
    }

    public void setInsurCount(Double insurCount) {
        this.insurCount = insurCount;
    }

    public String getInsurEff() {
        return insurEff;
    }

    public void setInsurEff(String insurEff) {
        this.insurEff = insurEff;
    }

    public String getInsurExp() {
        return insurExp;
    }

    public void setInsurExp(String insurExp) {
        this.insurExp = insurExp;
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
        NetcarBaseInfoVehicleInsurance other = (NetcarBaseInfoVehicleInsurance) that;
        return (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getVehicleNo() == null ? other.getVehicleNo() == null : this.getVehicleNo().equals(other.getVehicleNo()))
            && (this.getInsurCom() == null ? other.getInsurCom() == null : this.getInsurCom().equals(other.getInsurCom()))
            && (this.getInsurNum() == null ? other.getInsurNum() == null : this.getInsurNum().equals(other.getInsurNum()))
            && (this.getInsurType() == null ? other.getInsurType() == null : this.getInsurType().equals(other.getInsurType()))
            && (this.getInsurCount() == null ? other.getInsurCount() == null : this.getInsurCount().equals(other.getInsurCount()))
            && (this.getInsurEff() == null ? other.getInsurEff() == null : this.getInsurEff().equals(other.getInsurEff()))
            && (this.getInsurExp() == null ? other.getInsurExp() == null : this.getInsurExp().equals(other.getInsurExp()))
            && (this.getFlag() == null ? other.getFlag() == null : this.getFlag().equals(other.getFlag()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getReserved() == null ? other.getReserved() == null : this.getReserved().equals(other.getReserved()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getVehicleNo() == null) ? 0 : getVehicleNo().hashCode());
        result = prime * result + ((getInsurCom() == null) ? 0 : getInsurCom().hashCode());
        result = prime * result + ((getInsurNum() == null) ? 0 : getInsurNum().hashCode());
        result = prime * result + ((getInsurType() == null) ? 0 : getInsurType().hashCode());
        result = prime * result + ((getInsurCount() == null) ? 0 : getInsurCount().hashCode());
        result = prime * result + ((getInsurEff() == null) ? 0 : getInsurEff().hashCode());
        result = prime * result + ((getInsurExp() == null) ? 0 : getInsurExp().hashCode());
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
        sb.append(", vehicleNo=").append(vehicleNo);
        sb.append(", insurCom=").append(insurCom);
        sb.append(", insurNum=").append(insurNum);
        sb.append(", insurType=").append(insurType);
        sb.append(", insurCount=").append(insurCount);
        sb.append(", insurEff=").append(insurEff);
        sb.append(", insurExp=").append(insurExp);
        sb.append(", flag=").append(flag);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", reserved=").append(reserved);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}