package com.zhcx.netcar.pojo.DruidEntity;

import com.zhcx.netcar.annotation.FieldMeta;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/6/3 0003 15:54
 **/
public class OrderStatisticsResult implements Serializable {

    private static final long serialVersionUID = 2466796769412684373L;
    @FieldMeta(name = "企业名称", description = "企业名称", index = 0)
    private String companyName;

    private String companyId;
    @FieldMeta(name = "营运总趟次", description = "营运总趟次", index = 1)//总趟次/总订单数
    private Integer orderCount;
    @FieldMeta(name = "营运总金额", description = "营运总金额", index = 2)//总金额
    private Double factPrice;
    @FieldMeta(name = "总载客时间", description = "总载客时间", index = 10)//总载客时间
    private Integer driveTime;
    @FieldMeta(name = "总空驶时间", description = "总空驶时间", index = 9) //总空驶时间
    private Integer waitTime;
    @FieldMeta(name = "总载客里程", description = "总载客里程", index = 3)//总载客里程
    private Double driveMile;
    @FieldMeta(name = "载运里程（KM）", description = "载运里程（KM）", index = 4)//总载客里程
    private Double carryMile;
    @FieldMeta(name = "单趟平均营运金额（元）", description = "单趟平均营运金额（元）", index = 8)//总载客里程
    private Double averagePrice;
    @FieldMeta(name = "总空驶里程", description = "总空驶里程", index = 0)//总空驶里程
    private Double waitMile;
    @FieldMeta(name = "营运趟次", description = "营运趟次", index = 0)//营运趟次/订单总数
    private Integer total;
    @FieldMeta(name = "车均营运趟次", description = "车均营运趟次", index = 5)//车均营运趟次
    private Integer averageCount;
    @FieldMeta(name = "车均营运金额(月)", description = "车均营运金额(月)", index = 6) //车均营运金额(月)
    private Double averagePriceMM;
    @FieldMeta(name = "车均营运金额（日）", description = "车均营运金额（日）", index = 7)//车均营运金额（日）
    private Double averagePriceDD;
    @FieldMeta(name = "车均营运时间", description = "车均营运时间", index = 11)//车均营运时间
    private Integer averageTime;
    @FieldMeta(name = "车均空驶时间", description = "车均空驶时间", index = 0)//车均空驶时间
    private Integer averageTimeEmpty;
    @FieldMeta(name = "车均营运里程", description = "车均营运里程", index = 12)//车均营运里程
    private Double averageDriveMile;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Double getFactPrice() {
        return factPrice;
    }

    public void setFactPrice(Double factPrice) {
        this.factPrice = factPrice;
    }

    public Integer getDriveTime() {
        return driveTime;
    }

    public void setDriveTime(Integer driveTime) {
        this.driveTime = driveTime;
    }

    public Integer getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Integer waitTime) {
        this.waitTime = waitTime;
    }

    public Double getDriveMile() {
        return driveMile;
    }

    public void setDriveMile(Double driveMile) {
        this.driveMile = driveMile;
    }

    public Double getWaitMile() {
        return waitMile;
    }

    public void setWaitMile(Double waitMile) {
        this.waitMile = waitMile;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getAverageCount() {
        return averageCount;
    }

    public void setAverageCount(Integer averageCount) {
        this.averageCount = averageCount;
    }

    public Double getAveragePriceMM() {
        return averagePriceMM;
    }

    public void setAveragePriceMM(Double averagePriceMM) {
        this.averagePriceMM = averagePriceMM;
    }

    public Double getAveragePriceDD() {
        return averagePriceDD;
    }

    public void setAveragePriceDD(Double averagePriceDD) {
        this.averagePriceDD = averagePriceDD;
    }

    public Integer getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(Integer averageTime) {
        this.averageTime = averageTime;
    }

    public Integer getAverageTimeEmpty() {
        return averageTimeEmpty;
    }

    public void setAverageTimeEmpty(Integer averageTimeEmpty) {
        this.averageTimeEmpty = averageTimeEmpty;
    }

    public Double getAverageDriveMile() {
        return averageDriveMile;
    }

    public void setAverageDriveMile(Double averageDriveMile) {
        this.averageDriveMile = averageDriveMile;
    }

    public Double getCarryMile() {
        return carryMile;
    }

    public void setCarryMile(Double carryMile) {
        this.carryMile = carryMile;
    }

    public Double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(Double averagePrice) {
        this.averagePrice = averagePrice;
    }

    @Override
    public String toString() {
        return "OrderStatisticsResult{" +
                "companyName='" + companyName + '\'' +
                ", companyId='" + companyId + '\'' +
                ", orderCount=" + orderCount +
                ", factPrice=" + factPrice +
                ", driveTime=" + driveTime +
                ", waitTime=" + waitTime +
                ", driveMile=" + driveMile +
                ", carryMile=" + carryMile +
                ", averagePrice=" + averagePrice +
                ", waitMile=" + waitMile +
                ", total=" + total +
                ", averageCount=" + averageCount +
                ", averagePriceMM=" + averagePriceMM +
                ", averagePriceDD=" + averagePriceDD +
                ", averageTime=" + averageTime +
                ", averageTimeEmpty=" + averageTimeEmpty +
                ", averageDriveMile=" + averageDriveMile +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderStatisticsResult that = (OrderStatisticsResult) o;
        return Objects.equals(companyName, that.companyName) &&
                Objects.equals(companyId, that.companyId) &&
                Objects.equals(orderCount, that.orderCount) &&
                Objects.equals(factPrice, that.factPrice) &&
                Objects.equals(driveTime, that.driveTime) &&
                Objects.equals(waitTime, that.waitTime) &&
                Objects.equals(driveMile, that.driveMile) &&
                Objects.equals(carryMile, that.carryMile) &&
                Objects.equals(averagePrice, that.averagePrice) &&
                Objects.equals(waitMile, that.waitMile) &&
                Objects.equals(total, that.total) &&
                Objects.equals(averageCount, that.averageCount) &&
                Objects.equals(averagePriceMM, that.averagePriceMM) &&
                Objects.equals(averagePriceDD, that.averagePriceDD) &&
                Objects.equals(averageTime, that.averageTime) &&
                Objects.equals(averageTimeEmpty, that.averageTimeEmpty) &&
                Objects.equals(averageDriveMile, that.averageDriveMile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyName, companyId, orderCount, factPrice, driveTime, waitTime, driveMile, carryMile, averagePrice, waitMile, total, averageCount, averagePriceMM, averagePriceDD, averageTime, averageTimeEmpty, averageDriveMile);
    }
}
