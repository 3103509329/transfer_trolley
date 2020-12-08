package com.zhcx.netcar.pojo.DruidEntity;

import java.io.Serializable;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/6/3 0003 14:09
 **/
public class NetcarStatisticsDistance implements Serializable {

    public String companyId;

    public String moment;

    public Double driveMileSum;

    public Double waitMileSum;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getMoment() {
        return moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }

    public Double getDriveMileSum() {
        return driveMileSum;
    }

    public void setDriveMileSum(Double driveMileSum) {
        this.driveMileSum = driveMileSum;
    }

    public Double getWaitMileSum() {
        return waitMileSum;
    }

    public void setWaitMileSum(Double waitMileSum) {
        this.waitMileSum = waitMileSum;
    }
}
