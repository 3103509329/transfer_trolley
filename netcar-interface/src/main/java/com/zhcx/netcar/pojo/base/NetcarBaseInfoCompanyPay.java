package com.zhcx.netcar.pojo.base;

import java.io.Serializable;

public class NetcarBaseInfoCompanyPay extends NetcarBasePojo implements Serializable {
    private String companyId;

    private String payName;

    private String payId;

    private String payType;

    private String payScope;

    private String prepareBank;

    private Integer countDate;

    private Integer state;

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

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayScope() {
        return payScope;
    }

    public void setPayScope(String payScope) {
        this.payScope = payScope;
    }

    public String getPrepareBank() {
        return prepareBank;
    }

    public void setPrepareBank(String prepareBank) {
        this.prepareBank = prepareBank;
    }

    public Integer getCountDate() {
        return countDate;
    }

    public void setCountDate(Integer countDate) {
        this.countDate = countDate;
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
        NetcarBaseInfoCompanyPay other = (NetcarBaseInfoCompanyPay) that;
        return (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getPayName() == null ? other.getPayName() == null : this.getPayName().equals(other.getPayName()))
            && (this.getPayId() == null ? other.getPayId() == null : this.getPayId().equals(other.getPayId()))
            && (this.getPayType() == null ? other.getPayType() == null : this.getPayType().equals(other.getPayType()))
            && (this.getPayScope() == null ? other.getPayScope() == null : this.getPayScope().equals(other.getPayScope()))
            && (this.getPrepareBank() == null ? other.getPrepareBank() == null : this.getPrepareBank().equals(other.getPrepareBank()))
            && (this.getCountDate() == null ? other.getCountDate() == null : this.getCountDate().equals(other.getCountDate()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
            && (this.getFlag() == null ? other.getFlag() == null : this.getFlag().equals(other.getFlag()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getPayName() == null) ? 0 : getPayName().hashCode());
        result = prime * result + ((getPayId() == null) ? 0 : getPayId().hashCode());
        result = prime * result + ((getPayType() == null) ? 0 : getPayType().hashCode());
        result = prime * result + ((getPayScope() == null) ? 0 : getPayScope().hashCode());
        result = prime * result + ((getPrepareBank() == null) ? 0 : getPrepareBank().hashCode());
        result = prime * result + ((getCountDate() == null) ? 0 : getCountDate().hashCode());
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
        sb.append(", payName=").append(payName);
        sb.append(", payId=").append(payId);
        sb.append(", payType=").append(payType);
        sb.append(", payScope=").append(payScope);
        sb.append(", prepareBank=").append(prepareBank);
        sb.append(", countDate=").append(countDate);
        sb.append(", state=").append(state);
        sb.append(", flag=").append(flag);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}