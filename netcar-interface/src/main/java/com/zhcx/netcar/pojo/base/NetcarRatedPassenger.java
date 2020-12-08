package com.zhcx.netcar.pojo.base;

import java.io.Serializable;

public class NetcarRatedPassenger extends NetcarBasePojo implements Serializable {
    private String companyId;

    private String orderId;

    private String evaluateTime;

    private Double serviceScore;

    private Double driverScore;

    private Double vehicleScore;

    private String detail;

    private String reserved;

    private static final long serialVersionUID = 1L;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(String evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public Double getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(Double serviceScore) {
        this.serviceScore = serviceScore;
    }

    public Double getDriverScore() {
        return driverScore;
    }

    public void setDriverScore(Double driverScore) {
        this.driverScore = driverScore;
    }

    public Double getVehicleScore() {
        return vehicleScore;
    }

    public void setVehicleScore(Double vehicleScore) {
        this.vehicleScore = vehicleScore;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
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
        NetcarRatedPassenger other = (NetcarRatedPassenger) that;
        return (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getEvaluateTime() == null ? other.getEvaluateTime() == null : this.getEvaluateTime().equals(other.getEvaluateTime()))
            && (this.getServiceScore() == null ? other.getServiceScore() == null : this.getServiceScore().equals(other.getServiceScore()))
            && (this.getDriverScore() == null ? other.getDriverScore() == null : this.getDriverScore().equals(other.getDriverScore()))
            && (this.getVehicleScore() == null ? other.getVehicleScore() == null : this.getVehicleScore().equals(other.getVehicleScore()))
            && (this.getDetail() == null ? other.getDetail() == null : this.getDetail().equals(other.getDetail()))
            && (this.getReserved() == null ? other.getReserved() == null : this.getReserved().equals(other.getReserved()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getEvaluateTime() == null) ? 0 : getEvaluateTime().hashCode());
        result = prime * result + ((getServiceScore() == null) ? 0 : getServiceScore().hashCode());
        result = prime * result + ((getDriverScore() == null) ? 0 : getDriverScore().hashCode());
        result = prime * result + ((getVehicleScore() == null) ? 0 : getVehicleScore().hashCode());
        result = prime * result + ((getDetail() == null) ? 0 : getDetail().hashCode());
        result = prime * result + ((getReserved() == null) ? 0 : getReserved().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", companyId=").append(companyId);
        sb.append(", orderId=").append(orderId);
        sb.append(", evaluateTime=").append(evaluateTime);
        sb.append(", serviceScore=").append(serviceScore);
        sb.append(", driverScore=").append(driverScore);
        sb.append(", vehicleScore=").append(vehicleScore);
        sb.append(", detail=").append(detail);
        sb.append(", reserved=").append(reserved);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}