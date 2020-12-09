package com.zhcx.netcar.pojo.base;

import java.io.Serializable;

public class NetcarShareOrder extends NetcarBasePojo implements Serializable {
    private String companyId;

    private String routeId;

    private String orderId;

    private Integer address;

    private String bookDepartTime;

    private String departure;

    private Double depLongitude;

    private Double depLatitude;

    private String destination;

    private Double destLongitude;

    private Double destLatitude;

    private Integer encrypt;

    private String orderEnsureTime;

    private Integer passengerNum;

    private String passengerNote;

    private String reserved;

    private static final long serialVersionUID = 1L;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
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

    public String getBookDepartTime() {
        return bookDepartTime;
    }

    public void setBookDepartTime(String bookDepartTime) {
        this.bookDepartTime = bookDepartTime;
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

    public String getOrderEnsureTime() {
        return orderEnsureTime;
    }

    public void setOrderEnsureTime(String orderEnsureTime) {
        this.orderEnsureTime = orderEnsureTime;
    }

    public Integer getPassengerNum() {
        return passengerNum;
    }

    public void setPassengerNum(Integer passengerNum) {
        this.passengerNum = passengerNum;
    }

    public String getPassengerNote() {
        return passengerNote;
    }

    public void setPassengerNote(String passengerNote) {
        this.passengerNote = passengerNote;
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
        NetcarShareOrder other = (NetcarShareOrder) that;
        return (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getRouteId() == null ? other.getRouteId() == null : this.getRouteId().equals(other.getRouteId()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getBookDepartTime() == null ? other.getBookDepartTime() == null : this.getBookDepartTime().equals(other.getBookDepartTime()))
            && (this.getDeparture() == null ? other.getDeparture() == null : this.getDeparture().equals(other.getDeparture()))
            && (this.getDepLongitude() == null ? other.getDepLongitude() == null : this.getDepLongitude().equals(other.getDepLongitude()))
            && (this.getDepLatitude() == null ? other.getDepLatitude() == null : this.getDepLatitude().equals(other.getDepLatitude()))
            && (this.getDestination() == null ? other.getDestination() == null : this.getDestination().equals(other.getDestination()))
            && (this.getDestLongitude() == null ? other.getDestLongitude() == null : this.getDestLongitude().equals(other.getDestLongitude()))
            && (this.getDestLatitude() == null ? other.getDestLatitude() == null : this.getDestLatitude().equals(other.getDestLatitude()))
            && (this.getEncrypt() == null ? other.getEncrypt() == null : this.getEncrypt().equals(other.getEncrypt()))
            && (this.getOrderEnsureTime() == null ? other.getOrderEnsureTime() == null : this.getOrderEnsureTime().equals(other.getOrderEnsureTime()))
            && (this.getPassengerNum() == null ? other.getPassengerNum() == null : this.getPassengerNum().equals(other.getPassengerNum()))
            && (this.getPassengerNote() == null ? other.getPassengerNote() == null : this.getPassengerNote().equals(other.getPassengerNote()))
            && (this.getReserved() == null ? other.getReserved() == null : this.getReserved().equals(other.getReserved()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getRouteId() == null) ? 0 : getRouteId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getBookDepartTime() == null) ? 0 : getBookDepartTime().hashCode());
        result = prime * result + ((getDeparture() == null) ? 0 : getDeparture().hashCode());
        result = prime * result + ((getDepLongitude() == null) ? 0 : getDepLongitude().hashCode());
        result = prime * result + ((getDepLatitude() == null) ? 0 : getDepLatitude().hashCode());
        result = prime * result + ((getDestination() == null) ? 0 : getDestination().hashCode());
        result = prime * result + ((getDestLongitude() == null) ? 0 : getDestLongitude().hashCode());
        result = prime * result + ((getDestLatitude() == null) ? 0 : getDestLatitude().hashCode());
        result = prime * result + ((getEncrypt() == null) ? 0 : getEncrypt().hashCode());
        result = prime * result + ((getOrderEnsureTime() == null) ? 0 : getOrderEnsureTime().hashCode());
        result = prime * result + ((getPassengerNum() == null) ? 0 : getPassengerNum().hashCode());
        result = prime * result + ((getPassengerNote() == null) ? 0 : getPassengerNote().hashCode());
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
        sb.append(", routeId=").append(routeId);
        sb.append(", orderId=").append(orderId);
        sb.append(", address=").append(address);
        sb.append(", bookDepartTime=").append(bookDepartTime);
        sb.append(", departure=").append(departure);
        sb.append(", depLongitude=").append(depLongitude);
        sb.append(", depLatitude=").append(depLatitude);
        sb.append(", destination=").append(destination);
        sb.append(", destLongitude=").append(destLongitude);
        sb.append(", destLatitude=").append(destLatitude);
        sb.append(", encrypt=").append(encrypt);
        sb.append(", orderEnsureTime=").append(orderEnsureTime);
        sb.append(", passengerNum=").append(passengerNum);
        sb.append(", passengerNote=").append(passengerNote);
        sb.append(", reserved=").append(reserved);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}