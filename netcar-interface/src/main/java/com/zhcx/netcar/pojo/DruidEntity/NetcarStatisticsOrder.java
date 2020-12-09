package com.zhcx.netcar.pojo.DruidEntity;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/6/3 0003 14:09
 **/
public class NetcarStatisticsOrder implements Serializable {
    private static final long serialVersionUID = -782988396941180916L;
    public String COMPANY_ID;

    public String ON_AREA;

    public String PAY_TIME;

    public String VEHICLE_NO;

    public Double drive_mile_sum;

    public Integer drive_time_sum;

    public Double fact_price_sum;

    public Double wait_mile_sum;

    public Integer wait_time_sum;

    public Integer order_count;

    public String getCOMPANY_ID() {
        return COMPANY_ID;
    }

    public void setCOMPANY_ID(String COMPANY_ID) {
        this.COMPANY_ID = COMPANY_ID;
    }

    public String getON_AREA() {
        return ON_AREA;
    }

    public void setON_AREA(String ON_AREA) {
        this.ON_AREA = ON_AREA;
    }

    public String getPAY_TIME() {
        return PAY_TIME;
    }

    public void setPAY_TIME(String PAY_TIME) {
        this.PAY_TIME = PAY_TIME;
    }

    public String getVEHICLE_NO() {
        return VEHICLE_NO;
    }

    public void setVEHICLE_NO(String VEHICLE_NO) {
        this.VEHICLE_NO = VEHICLE_NO;
    }

    public Double getDrive_mile_sum() {
        return drive_mile_sum;
    }

    public void setDrive_mile_sum(Double drive_mile_sum) {
        this.drive_mile_sum = drive_mile_sum;
    }

    public Integer getDrive_time_sum() {
        return drive_time_sum;
    }

    public void setDrive_time_sum(Integer drive_time_sum) {
        this.drive_time_sum = drive_time_sum;
    }

    public Double getFact_price_sum() {
        return fact_price_sum;
    }

    public void setFact_price_sum(Double fact_price_sum) {
        this.fact_price_sum = fact_price_sum;
    }

    public Double getWait_mile_sum() {
        return wait_mile_sum;
    }

    public void setWait_mile_sum(Double wait_mile_sum) {
        this.wait_mile_sum = wait_mile_sum;
    }

    public Integer getWait_time_sum() {
        return wait_time_sum;
    }

    public void setWait_time_sum(Integer wait_time_sum) {
        this.wait_time_sum = wait_time_sum;
    }

    public Integer getOrder_count() {
        return order_count;
    }

    public void setOrder_count(Integer order_count) {
        this.order_count = order_count;
    }

    @Override
    public String toString() {
        return "NetcarStatisticsOrder{" +
                "COMPANY_ID='" + COMPANY_ID + '\'' +
                ", ON_AREA='" + ON_AREA + '\'' +
                ", PAY_TIME=" + PAY_TIME +
                ", VEHICLE_NO='" + VEHICLE_NO + '\'' +
                ", drive_mile_sum=" + drive_mile_sum +
                ", drive_time_sum=" + drive_time_sum +
                ", fact_price_sum=" + fact_price_sum +
                ", wait_mile_sum=" + wait_mile_sum +
                ", wait_time_sum=" + wait_time_sum +
                ", order_count=" + order_count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NetcarStatisticsOrder that = (NetcarStatisticsOrder) o;
        return Objects.equals(COMPANY_ID, that.COMPANY_ID) &&
                Objects.equals(ON_AREA, that.ON_AREA) &&
                Objects.equals(PAY_TIME, that.PAY_TIME) &&
                Objects.equals(VEHICLE_NO, that.VEHICLE_NO) &&
                Objects.equals(drive_mile_sum, that.drive_mile_sum) &&
                Objects.equals(drive_time_sum, that.drive_time_sum) &&
                Objects.equals(fact_price_sum, that.fact_price_sum) &&
                Objects.equals(wait_mile_sum, that.wait_mile_sum) &&
                Objects.equals(wait_time_sum, that.wait_time_sum) &&
                Objects.equals(order_count, that.order_count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(COMPANY_ID, ON_AREA, PAY_TIME, VEHICLE_NO, drive_mile_sum, drive_time_sum, fact_price_sum, wait_mile_sum, wait_time_sum, order_count);
    }
}
