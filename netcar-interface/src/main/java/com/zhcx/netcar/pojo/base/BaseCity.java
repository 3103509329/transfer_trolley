package com.zhcx.netcar.pojo.base;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 城市
 * @author Lee
 */
public class BaseCity  extends BasePojo implements Serializable {
    private Long uuid;

    private Long corpId;

    private String createBy;

    private Date createTime;

    private Date updateTime;

    private String updateBy;

    private String settlementName;

    private String settlementXypoint;

    private String cityPy;

    private String cityName;

    private Integer isHot;

    private Long province;

    private List<NetcarBaseInfoCompany> companyList;

    private List<BaseDistrict> children;

    private static final long serialVersionUID = 1L;

    public BaseCity(Long uuid, Long corpId, String createBy, Date createTime, Date updateTime, String updateBy, String settlementName, String settlementXypoint, String cityPy, String cityName, Integer isHot, Long province) {
        this.uuid = uuid;
        this.corpId = corpId;
        this.createBy = createBy;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.updateBy = updateBy;
        this.settlementName = settlementName;
        this.settlementXypoint = settlementXypoint;
        this.cityPy = cityPy;
        this.cityName = cityName;
        this.isHot = isHot;
        this.province = province;
    }

    public BaseCity() {
        super();
    }

    public List<NetcarBaseInfoCompany> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<NetcarBaseInfoCompany> companyList) {
        this.companyList = companyList;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Long getCorpId() {
        return corpId;
    }

    public void setCorpId(Long corpId) {
        this.corpId = corpId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public String getSettlementName() {
        return settlementName;
    }

    public void setSettlementName(String settlementName) {
        this.settlementName = settlementName == null ? null : settlementName.trim();
    }

    public String getSettlementXypoint() {
        return settlementXypoint;
    }

    public void setSettlementXypoint(String settlementXypoint) {
        this.settlementXypoint = settlementXypoint == null ? null : settlementXypoint.trim();
    }

    public String getCityPy() {
        return cityPy;
    }

    public void setCityPy(String cityPy) {
        this.cityPy = cityPy == null ? null : cityPy.trim();
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
    }

    public Integer getIsHot() {
        return isHot;
    }

    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }

    public Long getProvince() {
        return province;
    }

    public void setProvince(Long province) {
        this.province = province;
    }

    public List<BaseDistrict> getChildren() {
        return children;
    }

    public void setChildren(List<BaseDistrict> children) {
        this.children = children;
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
        BaseCity other = (BaseCity) that;
        return (this.getUuid() == null ? other.getUuid() == null : this.getUuid().equals(other.getUuid()))
            && (this.getCorpId() == null ? other.getCorpId() == null : this.getCorpId().equals(other.getCorpId()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getSettlementName() == null ? other.getSettlementName() == null : this.getSettlementName().equals(other.getSettlementName()))
            && (this.getSettlementXypoint() == null ? other.getSettlementXypoint() == null : this.getSettlementXypoint().equals(other.getSettlementXypoint()))
            && (this.getCityPy() == null ? other.getCityPy() == null : this.getCityPy().equals(other.getCityPy()))
            && (this.getCityName() == null ? other.getCityName() == null : this.getCityName().equals(other.getCityName()))
            && (this.getIsHot() == null ? other.getIsHot() == null : this.getIsHot().equals(other.getIsHot()))
            && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUuid() == null) ? 0 : getUuid().hashCode());
        result = prime * result + ((getCorpId() == null) ? 0 : getCorpId().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getSettlementName() == null) ? 0 : getSettlementName().hashCode());
        result = prime * result + ((getSettlementXypoint() == null) ? 0 : getSettlementXypoint().hashCode());
        result = prime * result + ((getCityPy() == null) ? 0 : getCityPy().hashCode());
        result = prime * result + ((getCityName() == null) ? 0 : getCityName().hashCode());
        result = prime * result + ((getIsHot() == null) ? 0 : getIsHot().hashCode());
        result = prime * result + ((getProvince() == null) ? 0 : getProvince().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", uuid=").append(uuid);
        sb.append(", corpId=").append(corpId);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", settlementName=").append(settlementName);
        sb.append(", settlementXypoint=").append(settlementXypoint);
        sb.append(", cityPy=").append(cityPy);
        sb.append(", cityName=").append(cityName);
        sb.append(", isHot=").append(isHot);
        sb.append(", province=").append(province);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}