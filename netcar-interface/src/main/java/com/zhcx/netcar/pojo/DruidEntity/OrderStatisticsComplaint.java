package com.zhcx.netcar.pojo.DruidEntity;

import java.io.Serializable;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/6/8 0008 16:55
 **/
public class OrderStatisticsComplaint implements Serializable {

    private String companyId;

    private String complaintTime;

    private Integer complaintCount;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getComplaintTime() {
        return complaintTime;
    }

    public void setComplaintTime(String complaintTime) {
        this.complaintTime = complaintTime;
    }

    public Integer getComplaintCount() {
        return complaintCount;
    }

    public void setComplaintCount(Integer complaintCount) {
        this.complaintCount = complaintCount;
    }
}
