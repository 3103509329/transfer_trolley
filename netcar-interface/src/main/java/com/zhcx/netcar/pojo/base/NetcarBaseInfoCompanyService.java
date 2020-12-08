package com.zhcx.netcar.pojo.base;

import java.io.Serializable;
import java.util.List;

public class NetcarBaseInfoCompanyService extends NetcarBasePojo implements Serializable {
    private String companyId;

    private Integer address;

    private String serviceName;

    private String serviceNo;

    private String detailAddress;

    private String responsibleName;

    private String responsiblePhone;

    private String managerName;

    private String managerPhone;

    private String contactPhone;

    private String mailAddress;

    private String createDate;

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

    public Integer getAddress() {
        return address;
    }

    public void setAddress(Integer address) {
        this.address = address;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getResponsibleName() {
        return responsibleName;
    }

    public void setResponsibleName(String responsibleName) {
        this.responsibleName = responsibleName;
    }

    public String getResponsiblePhone() {
        return responsiblePhone;
    }

    public void setResponsiblePhone(String responsiblePhone) {
        this.responsiblePhone = responsiblePhone;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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
        NetcarBaseInfoCompanyService other = (NetcarBaseInfoCompanyService) that;
        return (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getServiceName() == null ? other.getServiceName() == null : this.getServiceName().equals(other.getServiceName()))
            && (this.getServiceNo() == null ? other.getServiceNo() == null : this.getServiceNo().equals(other.getServiceNo()))
            && (this.getDetailAddress() == null ? other.getDetailAddress() == null : this.getDetailAddress().equals(other.getDetailAddress()))
            && (this.getResponsibleName() == null ? other.getResponsibleName() == null : this.getResponsibleName().equals(other.getResponsibleName()))
            && (this.getResponsiblePhone() == null ? other.getResponsiblePhone() == null : this.getResponsiblePhone().equals(other.getResponsiblePhone()))
            && (this.getManagerName() == null ? other.getManagerName() == null : this.getManagerName().equals(other.getManagerName()))
            && (this.getManagerPhone() == null ? other.getManagerPhone() == null : this.getManagerPhone().equals(other.getManagerPhone()))
            && (this.getContactPhone() == null ? other.getContactPhone() == null : this.getContactPhone().equals(other.getContactPhone()))
            && (this.getMailAddress() == null ? other.getMailAddress() == null : this.getMailAddress().equals(other.getMailAddress()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
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
        result = prime * result + ((getServiceName() == null) ? 0 : getServiceName().hashCode());
        result = prime * result + ((getServiceNo() == null) ? 0 : getServiceNo().hashCode());
        result = prime * result + ((getDetailAddress() == null) ? 0 : getDetailAddress().hashCode());
        result = prime * result + ((getResponsibleName() == null) ? 0 : getResponsibleName().hashCode());
        result = prime * result + ((getResponsiblePhone() == null) ? 0 : getResponsiblePhone().hashCode());
        result = prime * result + ((getManagerName() == null) ? 0 : getManagerName().hashCode());
        result = prime * result + ((getManagerPhone() == null) ? 0 : getManagerPhone().hashCode());
        result = prime * result + ((getContactPhone() == null) ? 0 : getContactPhone().hashCode());
        result = prime * result + ((getMailAddress() == null) ? 0 : getMailAddress().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
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
        sb.append(", serviceName=").append(serviceName);
        sb.append(", serviceNo=").append(serviceNo);
        sb.append(", detailAddress=").append(detailAddress);
        sb.append(", responsibleName=").append(responsibleName);
        sb.append(", responsiblePhone=").append(responsiblePhone);
        sb.append(", managerName=").append(managerName);
        sb.append(", managerPhone=").append(managerPhone);
        sb.append(", contactPhone=").append(contactPhone);
        sb.append(", mailAddress=").append(mailAddress);
        sb.append(", createDate=").append(createDate);
        sb.append(", state=").append(state);
        sb.append(", flag=").append(flag);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }


    /**
     * 添加属性 用于前端显示树形结构
     */
    private String tag;
    /**
     * 添加属性 用于前端显示树形结构
     */
    private String label;

    private List<NetcarBaseInfoVehicle> vehicleList;

    private List<NetcarBaseInfoVehicle> children;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<NetcarBaseInfoVehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<NetcarBaseInfoVehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public List<NetcarBaseInfoVehicle> getChildren() {
        return children;
    }

    public void setChildren(List<NetcarBaseInfoVehicle> children) {
        this.children = children;
    }
}