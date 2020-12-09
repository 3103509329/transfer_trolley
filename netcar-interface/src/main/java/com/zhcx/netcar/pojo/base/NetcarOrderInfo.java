package com.zhcx.netcar.pojo.base;

import java.io.Serializable;

public class NetcarOrderInfo extends NetcarBasePojo implements Serializable {
    private String companyId;

    private String orderId;

    private Integer address;

    private String departTime;

    private String orderTime;

    private String departure;

    private Double depLongitude;

    private Double depLatitude;

    private String destination;

    private Double destLongitude;

    private Double destLatitude;

    private Integer encrypt;

    private String fareType;

    private Double longitude;

    private Double latitude;

    private String licenseId;

    private String driverPhone;

    private String vehicleNo;

    private String distributeTime;

    private String cancelTime;

    private String operator;

    private String cancelTypeCode;

    private String cancelReason;

    private String passengerNote;

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

    public Integer getAddress() {
        return address;
    }

    public void setAddress(Integer address) {
        this.address = address;
    }

    public String getDepartTime() {
        return departTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public Double getDepLongitude() {
        return depLongitude;
    }

    public void setDepLongitude(Double depLongitude) {
        this.depLongitude = depLongitude;
    }

    public Double getDepLatitude() {
        return depLatitude;
    }

    public void setDepLatitude(Double depLatitude) {
        this.depLatitude = depLatitude;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Double getDestLongitude() {
        return destLongitude;
    }

    public void setDestLongitude(Double destLongitude) {
        this.destLongitude = destLongitude;
    }

    public Double getDestLatitude() {
        return destLatitude;
    }

    public void setDestLatitude(Double destLatitude) {
        this.destLatitude = destLatitude;
    }

    public Integer getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(Integer encrypt) {
        this.encrypt = encrypt;
    }

    public String getFareType() {
        return fareType;
    }

    public void setFareType(String fareType) {
        this.fareType = fareType;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getDistributeTime() {
        return distributeTime;
    }

    public void setDistributeTime(String distributeTime) {
        this.distributeTime = distributeTime;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCancelTypeCode() {
        return cancelTypeCode;
    }

    public void setCancelTypeCode(String cancelTypeCode) {
        this.cancelTypeCode = cancelTypeCode;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getPassengerNote() {
        return passengerNote;
    }

    public void setPassengerNote(String passengerNote) {
        this.passengerNote = passengerNote;
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
        NetcarOrderInfo other = (NetcarOrderInfo) that;
        return (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getDepartTime() == null ? other.getDepartTime() == null : this.getDepartTime().equals(other.getDepartTime()))
            && (this.getOrderTime() == null ? other.getOrderTime() == null : this.getOrderTime().equals(other.getOrderTime()))
            && (this.getDeparture() == null ? other.getDeparture() == null : this.getDeparture().equals(other.getDeparture()))
            && (this.getDepLongitude() == null ? other.getDepLongitude() == null : this.getDepLongitude().equals(other.getDepLongitude()))
            && (this.getDepLatitude() == null ? other.getDepLatitude() == null : this.getDepLatitude().equals(other.getDepLatitude()))
            && (this.getDestination() == null ? other.getDestination() == null : this.getDestination().equals(other.getDestination()))
            && (this.getDestLongitude() == null ? other.getDestLongitude() == null : this.getDestLongitude().equals(other.getDestLongitude()))
            && (this.getDestLatitude() == null ? other.getDestLatitude() == null : this.getDestLatitude().equals(other.getDestLatitude()))
            && (this.getEncrypt() == null ? other.getEncrypt() == null : this.getEncrypt().equals(other.getEncrypt()))
            && (this.getFareType() == null ? other.getFareType() == null : this.getFareType().equals(other.getFareType()))
            && (this.getLongitude() == null ? other.getLongitude() == null : this.getLongitude().equals(other.getLongitude()))
            && (this.getLatitude() == null ? other.getLatitude() == null : this.getLatitude().equals(other.getLatitude()))
            && (this.getLicenseId() == null ? other.getLicenseId() == null : this.getLicenseId().equals(other.getLicenseId()))
            && (this.getDriverPhone() == null ? other.getDriverPhone() == null : this.getDriverPhone().equals(other.getDriverPhone()))
            && (this.getVehicleNo() == null ? other.getVehicleNo() == null : this.getVehicleNo().equals(other.getVehicleNo()))
            && (this.getDistributeTime() == null ? other.getDistributeTime() == null : this.getDistributeTime().equals(other.getDistributeTime()))
            && (this.getCancelTime() == null ? other.getCancelTime() == null : this.getCancelTime().equals(other.getCancelTime()))
            && (this.getOperator() == null ? other.getOperator() == null : this.getOperator().equals(other.getOperator()))
            && (this.getCancelTypeCode() == null ? other.getCancelTypeCode() == null : this.getCancelTypeCode().equals(other.getCancelTypeCode()))
            && (this.getCancelReason() == null ? other.getCancelReason() == null : this.getCancelReason().equals(other.getCancelReason()))
            && (this.getPassengerNote() == null ? other.getPassengerNote() == null : this.getPassengerNote().equals(other.getPassengerNote()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getDepartTime() == null) ? 0 : getDepartTime().hashCode());
        result = prime * result + ((getOrderTime() == null) ? 0 : getOrderTime().hashCode());
        result = prime * result + ((getDeparture() == null) ? 0 : getDeparture().hashCode());
        result = prime * result + ((getDepLongitude() == null) ? 0 : getDepLongitude().hashCode());
        result = prime * result + ((getDepLatitude() == null) ? 0 : getDepLatitude().hashCode());
        result = prime * result + ((getDestination() == null) ? 0 : getDestination().hashCode());
        result = prime * result + ((getDestLongitude() == null) ? 0 : getDestLongitude().hashCode());
        result = prime * result + ((getDestLatitude() == null) ? 0 : getDestLatitude().hashCode());
        result = prime * result + ((getEncrypt() == null) ? 0 : getEncrypt().hashCode());
        result = prime * result + ((getFareType() == null) ? 0 : getFareType().hashCode());
        result = prime * result + ((getLongitude() == null) ? 0 : getLongitude().hashCode());
        result = prime * result + ((getLatitude() == null) ? 0 : getLatitude().hashCode());
        result = prime * result + ((getLicenseId() == null) ? 0 : getLicenseId().hashCode());
        result = prime * result + ((getDriverPhone() == null) ? 0 : getDriverPhone().hashCode());
        result = prime * result + ((getVehicleNo() == null) ? 0 : getVehicleNo().hashCode());
        result = prime * result + ((getDistributeTime() == null) ? 0 : getDistributeTime().hashCode());
        result = prime * result + ((getCancelTime() == null) ? 0 : getCancelTime().hashCode());
        result = prime * result + ((getOperator() == null) ? 0 : getOperator().hashCode());
        result = prime * result + ((getCancelTypeCode() == null) ? 0 : getCancelTypeCode().hashCode());
        result = prime * result + ((getCancelReason() == null) ? 0 : getCancelReason().hashCode());
        result = prime * result + ((getPassengerNote() == null) ? 0 : getPassengerNote().hashCode());
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
        sb.append(", address=").append(address);
        sb.append(", departTime=").append(departTime);
        sb.append(", orderTime=").append(orderTime);
        sb.append(", departure=").append(departure);
        sb.append(", depLongitude=").append(depLongitude);
        sb.append(", depLatitude=").append(depLatitude);
        sb.append(", destination=").append(destination);
        sb.append(", destLongitude=").append(destLongitude);
        sb.append(", destLatitude=").append(destLatitude);
        sb.append(", encrypt=").append(encrypt);
        sb.append(", fareType=").append(fareType);
        sb.append(", longitude=").append(longitude);
        sb.append(", latitude=").append(latitude);
        sb.append(", licenseId=").append(licenseId);
        sb.append(", driverPhone=").append(driverPhone);
        sb.append(", vehicleNo=").append(vehicleNo);
        sb.append(", distributeTime=").append(distributeTime);
        sb.append(", cancelTime=").append(cancelTime);
        sb.append(", operator=").append(operator);
        sb.append(", cancelTypeCode=").append(cancelTypeCode);
        sb.append(", cancelReason=").append(cancelReason);
        sb.append(", passengerNote=").append(passengerNote);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}