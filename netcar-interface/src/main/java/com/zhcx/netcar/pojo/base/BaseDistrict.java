package com.zhcx.netcar.pojo.base;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Lee
 * 地区
 */
public class BaseDistrict  extends BasePojo implements Serializable {
    private Long uuid;

    private String disName;

    private Long cityId;

    private Integer disSort;

    private Long corpId;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private List<NetcarBaseInfoCompanyService> children;

    private static final long serialVersionUID = 1L;

    public BaseDistrict(Long uuid, String disName, Long cityId, Integer disSort, Long corpId, String createBy, Date createTime, String updateBy, Date updateTime) {
        this.uuid = uuid;
        this.disName = disName;
        this.cityId = cityId;
        this.disSort = disSort;
        this.corpId = corpId;
        this.createBy = createBy;
        this.createTime = createTime;
        this.updateBy = updateBy;
        this.updateTime = updateTime;
    }

    public BaseDistrict() {
        super();
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getDisName() {
        return disName;
    }

    public void setDisName(String disName) {
        this.disName = disName == null ? null : disName.trim();
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Integer getDisSort() {
        return disSort;
    }

    public void setDisSort(Integer disSort) {
        this.disSort = disSort;
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

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<NetcarBaseInfoCompanyService> getChildren() {
        return children;
    }

    public void setChildren(List<NetcarBaseInfoCompanyService> children) {
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
        BaseDistrict other = (BaseDistrict) that;
        return (this.getUuid() == null ? other.getUuid() == null : this.getUuid().equals(other.getUuid()))
            && (this.getDisName() == null ? other.getDisName() == null : this.getDisName().equals(other.getDisName()))
            && (this.getCityId() == null ? other.getCityId() == null : this.getCityId().equals(other.getCityId()))
            && (this.getDisSort() == null ? other.getDisSort() == null : this.getDisSort().equals(other.getDisSort()))
            && (this.getCorpId() == null ? other.getCorpId() == null : this.getCorpId().equals(other.getCorpId()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUuid() == null) ? 0 : getUuid().hashCode());
        result = prime * result + ((getDisName() == null) ? 0 : getDisName().hashCode());
        result = prime * result + ((getCityId() == null) ? 0 : getCityId().hashCode());
        result = prime * result + ((getDisSort() == null) ? 0 : getDisSort().hashCode());
        result = prime * result + ((getCorpId() == null) ? 0 : getCorpId().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", uuid=").append(uuid);
        sb.append(", disName=").append(disName);
        sb.append(", cityId=").append(cityId);
        sb.append(", disSort=").append(disSort);
        sb.append(", corpId=").append(corpId);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}