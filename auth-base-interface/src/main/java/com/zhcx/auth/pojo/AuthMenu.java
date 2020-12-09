package com.zhcx.auth.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AuthMenu implements Serializable {

    private Long uuid;

    private Long creator;

    private Long whoModified;

    private String timeCreated;

    private String timeModified;

    private String menuCode;

    private String menuName;

    private String menuUrl;

    private Integer displayOrder;

    private String parentId;

    private String iconfont;

    private String status;

    private String str1;

    private String parentName;

    /**
     * 系统归属：1，网约车、2，出租车
     */
    private Integer menuSys;

    /**
     * 当前页码
     */
    private String pageNo;

    /**
     * 每一页的记录条数
     */
    private String pageSize;

    //查询菜单：菜单ID集合
    private Long[] queryMenuIds;

    private String orderBy = null;

    private String sortType = "ASC";

    private String applicationCode;

    private List<AuthMenu> menuList;
    private static final long serialVersionUID = 1L;

    public AuthMenu(Long uuid, Long creator, Long whoModified, String timeCreated, String timeModified, String menuCode, String menuName, String menuUrl, Integer displayOrder, String parentId, String iconfont, String status, String str1, String parentName, Integer menuSys, String pageNo, String pageSize, Long[] queryMenuIds, String orderBy, String sortType, String applicationCode) {
        this.uuid = uuid;
        this.creator = creator;
        this.whoModified = whoModified;
        this.timeCreated = timeCreated;
        this.timeModified = timeModified;
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuUrl = menuUrl;
        this.displayOrder = displayOrder;
        this.parentId = parentId;
        this.iconfont = iconfont;
        this.status = status;
        this.str1 = str1;
        this.parentName = parentName;
        this.menuSys = menuSys;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.queryMenuIds = queryMenuIds;
        this.orderBy = orderBy;
        this.sortType = sortType;
        this.applicationCode = applicationCode;
    }

    public AuthMenu() {
        super();
    }

    public AuthMenu(Long uuid, Long creator, Long whoModified, String timeCreated, String timeModified, String menuCode, String menuName, String menuUrl, Integer displayOrder, String parentId, String iconfont, String status, String str1, String parentName, Integer menuSys, String pageNo, String pageSize, Long[] queryMenuIds, String orderBy, String sortType, String applicationCode, List<AuthMenu> menuList) {
        this.uuid = uuid;
        this.creator = creator;
        this.whoModified = whoModified;
        this.timeCreated = timeCreated;
        this.timeModified = timeModified;
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuUrl = menuUrl;
        this.displayOrder = displayOrder;
        this.parentId = parentId;
        this.iconfont = iconfont;
        this.status = status;
        this.str1 = str1;
        this.parentName = parentName;
        this.menuSys = menuSys;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.queryMenuIds = queryMenuIds;
        this.orderBy = orderBy;
        this.sortType = sortType;
        this.applicationCode = applicationCode;
        this.menuList = menuList;
    }

    public List<AuthMenu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<AuthMenu> menuList) {
        this.menuList = menuList;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public Long[] getQueryMenuIds() {
        return queryMenuIds;
    }

    public void setQueryMenuIds(Long[] queryMenuIds) {
        this.queryMenuIds = queryMenuIds;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Long getWhoModified() {
        return whoModified;
    }

    public void setWhoModified(Long whoModified) {
        this.whoModified = whoModified;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated == null ? null : timeCreated.trim();
    }

    public String getTimeModified() {
        return timeModified;
    }

    public void setTimeModified(String timeModified) {
        this.timeModified = timeModified == null ? null : timeModified.trim();
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode == null ? null : menuCode.trim();
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl == null ? null : menuUrl.trim();
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getIconfont() {
        return iconfont;
    }

    public void setIconfont(String iconfont) {
        this.iconfont = iconfont == null ? null : iconfont.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1 == null ? null : str1.trim();
    }

    public Integer getMenuSys() {
        return menuSys;
    }

    public void setMenuSys(Integer menuSys) {
        this.menuSys = menuSys;
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
        AuthMenu other = (AuthMenu) that;
        return (this.getUuid() == null ? other.getUuid() == null : this.getUuid().equals(other.getUuid()))
                && (this.getCreator() == null ? other.getCreator() == null : this.getCreator().equals(other.getCreator()))
                && (this.getWhoModified() == null ? other.getWhoModified() == null : this.getWhoModified().equals(other.getWhoModified()))
                && (this.getTimeCreated() == null ? other.getTimeCreated() == null : this.getTimeCreated().equals(other.getTimeCreated()))
                && (this.getTimeModified() == null ? other.getTimeModified() == null : this.getTimeModified().equals(other.getTimeModified()))
                && (this.getMenuCode() == null ? other.getMenuCode() == null : this.getMenuCode().equals(other.getMenuCode()))
                && (this.getMenuName() == null ? other.getMenuName() == null : this.getMenuName().equals(other.getMenuName()))
                && (this.getMenuUrl() == null ? other.getMenuUrl() == null : this.getMenuUrl().equals(other.getMenuUrl()))
                && (this.getDisplayOrder() == null ? other.getDisplayOrder() == null : this.getDisplayOrder().equals(other.getDisplayOrder()))
                && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
                && (this.getIconfont() == null ? other.getIconfont() == null : this.getIconfont().equals(other.getIconfont()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getStr1() == null ? other.getStr1() == null : this.getStr1().equals(other.getStr1()))
                && (this.getMenuSys() == null ? other.getMenuSys() == null : this.getMenuSys().equals(other.getMenuSys()));

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUuid() == null) ? 0 : getUuid().hashCode());
        result = prime * result + ((getCreator() == null) ? 0 : getCreator().hashCode());
        result = prime * result + ((getWhoModified() == null) ? 0 : getWhoModified().hashCode());
        result = prime * result + ((getTimeCreated() == null) ? 0 : getTimeCreated().hashCode());
        result = prime * result + ((getTimeModified() == null) ? 0 : getTimeModified().hashCode());
        result = prime * result + ((getMenuCode() == null) ? 0 : getMenuCode().hashCode());
        result = prime * result + ((getMenuName() == null) ? 0 : getMenuName().hashCode());
        result = prime * result + ((getMenuUrl() == null) ? 0 : getMenuUrl().hashCode());
        result = prime * result + ((getDisplayOrder() == null) ? 0 : getDisplayOrder().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getIconfont() == null) ? 0 : getIconfont().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getStr1() == null) ? 0 : getStr1().hashCode());
        result = prime * result + ((getMenuSys() == null) ? 0 : getMenuSys().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", uuid=").append(uuid);
        sb.append(", creator=").append(creator);
        sb.append(", whoModified=").append(whoModified);
        sb.append(", timeCreated=").append(timeCreated);
        sb.append(", timeModified=").append(timeModified);
        sb.append(", menuCode=").append(menuCode);
        sb.append(", menuName=").append(menuName);
        sb.append(", menuUrl=").append(menuUrl);
        sb.append(", displayOrder=").append(displayOrder);
        sb.append(", parentId=").append(parentId);
        sb.append(", iconfont=").append(iconfont);
        sb.append(", status=").append(status);
        sb.append(", str1=").append(str1);
        sb.append(", menuSys=").append(menuSys);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}