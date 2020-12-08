package com.zhcx.netcar.params;

import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/7 13:19
 **/
public class BaseParam implements Serializable {

    private static final long serialVersionUID = -6411514650455608977L;

    /**
     * 公司标识
     */
    private String companyId;
    /**
     * 起始页
     */
    private Integer pageNo = 1;
    /**
     * 当前页数量
     */
    private Integer pageSize = 30;
    /**
     * 排序字段
     */
    private String orderBy;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "BaseParam{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", orderBy='" + orderBy + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
