package com.zhcx.netcar.pojo.base;

import java.io.Serializable;

public class NetcarBaseInfoCompanyPermit extends NetcarBasePojo implements Serializable {
    private String companyId;

    private Integer address;

    private String certificate;

    private String operationArea;

    private String ownerName;

    private String organization;

    private String startDate;

    private String stopDate;

    private String certifyDate;

    private String state;

    private Integer flag;

    private String updateTime;

    private String reserved;

    private static final long serialVersionUID = 1L;

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getAddress() {
        return address;
    }

    public void setAddress(Integer address) {
        this.address = address;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getOperationArea() {
        return operationArea;
    }

    public void setOperationArea(String operationArea) {
        this.operationArea = operationArea;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStopDate() {
        return stopDate;
    }

    public void setStopDate(String stopDate) {
        this.stopDate = stopDate;
    }

    public String getCertifyDate() {
        return certifyDate;
    }

    public void setCertifyDate(String certifyDate) {
        this.certifyDate = certifyDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
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
        NetcarBaseInfoCompanyPermit other = (NetcarBaseInfoCompanyPermit) that;
        return (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getCertificate() == null ? other.getCertificate() == null : this.getCertificate().equals(other.getCertificate()))
            && (this.getOperationArea() == null ? other.getOperationArea() == null : this.getOperationArea().equals(other.getOperationArea()))
            && (this.getOwnerName() == null ? other.getOwnerName() == null : this.getOwnerName().equals(other.getOwnerName()))
            && (this.getOrganization() == null ? other.getOrganization() == null : this.getOrganization().equals(other.getOrganization()))
            && (this.getStartDate() == null ? other.getStartDate() == null : this.getStartDate().equals(other.getStartDate()))
            && (this.getStopDate() == null ? other.getStopDate() == null : this.getStopDate().equals(other.getStopDate()))
            && (this.getCertifyDate() == null ? other.getCertifyDate() == null : this.getCertifyDate().equals(other.getCertifyDate()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
            && (this.getFlag() == null ? other.getFlag() == null : this.getFlag().equals(other.getFlag()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getCertificate() == null) ? 0 : getCertificate().hashCode());
        result = prime * result + ((getOperationArea() == null) ? 0 : getOperationArea().hashCode());
        result = prime * result + ((getOwnerName() == null) ? 0 : getOwnerName().hashCode());
        result = prime * result + ((getOrganization() == null) ? 0 : getOrganization().hashCode());
        result = prime * result + ((getStartDate() == null) ? 0 : getStartDate().hashCode());
        result = prime * result + ((getStopDate() == null) ? 0 : getStopDate().hashCode());
        result = prime * result + ((getCertifyDate() == null) ? 0 : getCertifyDate().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        result = prime * result + ((getFlag() == null) ? 0 : getFlag().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", companyId=").append(companyId);
        sb.append(", address=").append(address);
        sb.append(", certificate=").append(certificate);
        sb.append(", operationArea=").append(operationArea);
        sb.append(", ownerName=").append(ownerName);
        sb.append(", organization=").append(organization);
        sb.append(", startDate=").append(startDate);
        sb.append(", stopDate=").append(stopDate);
        sb.append(", certifyDate=").append(certifyDate);
        sb.append(", state=").append(state);
        sb.append(", flag=").append(flag);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}