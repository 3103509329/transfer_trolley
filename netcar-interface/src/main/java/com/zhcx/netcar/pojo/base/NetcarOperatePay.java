package com.zhcx.netcar.pojo.base;

import java.io.Serializable;

public class NetcarOperatePay extends NetcarBasePojo implements Serializable {
    private String companyId;

    private String orderId;

    private Integer onArea;

    private String driverName;

    private String licenseId;

    private String fareType;

    private String vehicleNo;

    private String bookDepTime;

    private Integer waitTime;

    private Double depLongitude;

    private Double depLatitude;

    private String depArea;

    private String depTime;

    private Double destLongitude;

    private Double destLatitude;

    private String destArea;

    private String destTime;

    private String bookModel;

    private String model;

    private Double driveMile =0D;

    private Integer driveTime;

    private Double waitMile;

    private Double factPrice;

    private Double price;

    private Double cashPrice;

    private String lineName;

    private Double linePrice;

    private String posName;

    private Double posPrice;

    private Double benfitPrice;

    private Double bookTip;

    private Double passengerTip;

    private Double peakUpPrice;

    private Double nightUpPrice;

    private Double farUpPrice;

    private Double otherUpPrice;

    private String payState;

    private String payTime;

    private String orderMatchTime;

    private Integer invoiceStatus;

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

    public Integer getOnArea() {
        return onArea;
    }

    public void setOnArea(Integer onArea) {
        this.onArea = onArea;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getFareType() {
        return fareType;
    }

    public void setFareType(String fareType) {
        this.fareType = fareType;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getBookDepTime() {
        return bookDepTime;
    }

    public void setBookDepTime(String bookDepTime) {
        this.bookDepTime = bookDepTime;
    }

    public Integer getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Integer waitTime) {
        this.waitTime = waitTime;
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

    public String getDepArea() {
        return depArea;
    }

    public void setDepArea(String depArea) {
        this.depArea = depArea;
    }

    public String getDepTime() {
        return depTime;
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
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

    public String getDestArea() {
        return destArea;
    }

    public void setDestArea(String destArea) {
        this.destArea = destArea;
    }

    public String getDestTime() {
        return destTime;
    }

    public void setDestTime(String destTime) {
        this.destTime = destTime;
    }

    public String getBookModel() {
        return bookModel;
    }

    public void setBookModel(String bookModel) {
        this.bookModel = bookModel;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getDriveMile() {
        return driveMile;
    }

    public void setDriveMile(Double driveMile) {
        this.driveMile = driveMile;
    }

    public Integer getDriveTime() {
        return driveTime;
    }

    public void setDriveTime(Integer driveTime) {
        this.driveTime = driveTime;
    }

    public Double getWaitMile() {
        return waitMile;
    }

    public void setWaitMile(Double waitMile) {
        this.waitMile = waitMile;
    }

    public Double getFactPrice() {
        return factPrice;
    }

    public void setFactPrice(Double factPrice) {
        this.factPrice = factPrice;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCashPrice() {
        return cashPrice;
    }

    public void setCashPrice(Double cashPrice) {
        this.cashPrice = cashPrice;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public Double getLinePrice() {
        return linePrice;
    }

    public void setLinePrice(Double linePrice) {
        this.linePrice = linePrice;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public Double getPosPrice() {
        return posPrice;
    }

    public void setPosPrice(Double posPrice) {
        this.posPrice = posPrice;
    }

    public Double getBenfitPrice() {
        return benfitPrice;
    }

    public void setBenfitPrice(Double benfitPrice) {
        this.benfitPrice = benfitPrice;
    }

    public Double getBookTip() {
        return bookTip;
    }

    public void setBookTip(Double bookTip) {
        this.bookTip = bookTip;
    }

    public Double getPassengerTip() {
        return passengerTip;
    }

    public void setPassengerTip(Double passengerTip) {
        this.passengerTip = passengerTip;
    }

    public Double getPeakUpPrice() {
        return peakUpPrice;
    }

    public void setPeakUpPrice(Double peakUpPrice) {
        this.peakUpPrice = peakUpPrice;
    }

    public Double getNightUpPrice() {
        return nightUpPrice;
    }

    public void setNightUpPrice(Double nightUpPrice) {
        this.nightUpPrice = nightUpPrice;
    }

    public Double getFarUpPrice() {
        return farUpPrice;
    }

    public void setFarUpPrice(Double farUpPrice) {
        this.farUpPrice = farUpPrice;
    }

    public Double getOtherUpPrice() {
        return otherUpPrice;
    }

    public void setOtherUpPrice(Double otherUpPrice) {
        this.otherUpPrice = otherUpPrice;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getOrderMatchTime() {
        return orderMatchTime;
    }

    public void setOrderMatchTime(String orderMatchTime) {
        this.orderMatchTime = orderMatchTime;
    }

    public Integer getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
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
        NetcarOperatePay other = (NetcarOperatePay) that;
        return (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getOnArea() == null ? other.getOnArea() == null : this.getOnArea().equals(other.getOnArea()))
            && (this.getDriverName() == null ? other.getDriverName() == null : this.getDriverName().equals(other.getDriverName()))
            && (this.getLicenseId() == null ? other.getLicenseId() == null : this.getLicenseId().equals(other.getLicenseId()))
            && (this.getFareType() == null ? other.getFareType() == null : this.getFareType().equals(other.getFareType()))
            && (this.getVehicleNo() == null ? other.getVehicleNo() == null : this.getVehicleNo().equals(other.getVehicleNo()))
            && (this.getBookDepTime() == null ? other.getBookDepTime() == null : this.getBookDepTime().equals(other.getBookDepTime()))
            && (this.getWaitTime() == null ? other.getWaitTime() == null : this.getWaitTime().equals(other.getWaitTime()))
            && (this.getDepLongitude() == null ? other.getDepLongitude() == null : this.getDepLongitude().equals(other.getDepLongitude()))
            && (this.getDepLatitude() == null ? other.getDepLatitude() == null : this.getDepLatitude().equals(other.getDepLatitude()))
            && (this.getDepArea() == null ? other.getDepArea() == null : this.getDepArea().equals(other.getDepArea()))
            && (this.getDepTime() == null ? other.getDepTime() == null : this.getDepTime().equals(other.getDepTime()))
            && (this.getDestLongitude() == null ? other.getDestLongitude() == null : this.getDestLongitude().equals(other.getDestLongitude()))
            && (this.getDestLatitude() == null ? other.getDestLatitude() == null : this.getDestLatitude().equals(other.getDestLatitude()))
            && (this.getDestArea() == null ? other.getDestArea() == null : this.getDestArea().equals(other.getDestArea()))
            && (this.getDestTime() == null ? other.getDestTime() == null : this.getDestTime().equals(other.getDestTime()))
            && (this.getBookModel() == null ? other.getBookModel() == null : this.getBookModel().equals(other.getBookModel()))
            && (this.getModel() == null ? other.getModel() == null : this.getModel().equals(other.getModel()))
            && (this.getDriveMile() == null ? other.getDriveMile() == null : this.getDriveMile().equals(other.getDriveMile()))
            && (this.getDriveTime() == null ? other.getDriveTime() == null : this.getDriveTime().equals(other.getDriveTime()))
            && (this.getWaitMile() == null ? other.getWaitMile() == null : this.getWaitMile().equals(other.getWaitMile()))
            && (this.getFactPrice() == null ? other.getFactPrice() == null : this.getFactPrice().equals(other.getFactPrice()))
            && (this.getPrice() == null ? other.getPrice() == null : this.getPrice().equals(other.getPrice()))
            && (this.getCashPrice() == null ? other.getCashPrice() == null : this.getCashPrice().equals(other.getCashPrice()))
            && (this.getLineName() == null ? other.getLineName() == null : this.getLineName().equals(other.getLineName()))
            && (this.getLinePrice() == null ? other.getLinePrice() == null : this.getLinePrice().equals(other.getLinePrice()))
            && (this.getPosName() == null ? other.getPosName() == null : this.getPosName().equals(other.getPosName()))
            && (this.getPosPrice() == null ? other.getPosPrice() == null : this.getPosPrice().equals(other.getPosPrice()))
            && (this.getBenfitPrice() == null ? other.getBenfitPrice() == null : this.getBenfitPrice().equals(other.getBenfitPrice()))
            && (this.getBookTip() == null ? other.getBookTip() == null : this.getBookTip().equals(other.getBookTip()))
            && (this.getPassengerTip() == null ? other.getPassengerTip() == null : this.getPassengerTip().equals(other.getPassengerTip()))
            && (this.getPeakUpPrice() == null ? other.getPeakUpPrice() == null : this.getPeakUpPrice().equals(other.getPeakUpPrice()))
            && (this.getNightUpPrice() == null ? other.getNightUpPrice() == null : this.getNightUpPrice().equals(other.getNightUpPrice()))
            && (this.getFarUpPrice() == null ? other.getFarUpPrice() == null : this.getFarUpPrice().equals(other.getFarUpPrice()))
            && (this.getOtherUpPrice() == null ? other.getOtherUpPrice() == null : this.getOtherUpPrice().equals(other.getOtherUpPrice()))
            && (this.getPayState() == null ? other.getPayState() == null : this.getPayState().equals(other.getPayState()))
            && (this.getPayTime() == null ? other.getPayTime() == null : this.getPayTime().equals(other.getPayTime()))
            && (this.getOrderMatchTime() == null ? other.getOrderMatchTime() == null : this.getOrderMatchTime().equals(other.getOrderMatchTime()))
            && (this.getInvoiceStatus() == null ? other.getInvoiceStatus() == null : this.getInvoiceStatus().equals(other.getInvoiceStatus()))
            && (this.getReserved() == null ? other.getReserved() == null : this.getReserved().equals(other.getReserved()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getOnArea() == null) ? 0 : getOnArea().hashCode());
        result = prime * result + ((getDriverName() == null) ? 0 : getDriverName().hashCode());
        result = prime * result + ((getLicenseId() == null) ? 0 : getLicenseId().hashCode());
        result = prime * result + ((getFareType() == null) ? 0 : getFareType().hashCode());
        result = prime * result + ((getVehicleNo() == null) ? 0 : getVehicleNo().hashCode());
        result = prime * result + ((getBookDepTime() == null) ? 0 : getBookDepTime().hashCode());
        result = prime * result + ((getWaitTime() == null) ? 0 : getWaitTime().hashCode());
        result = prime * result + ((getDepLongitude() == null) ? 0 : getDepLongitude().hashCode());
        result = prime * result + ((getDepLatitude() == null) ? 0 : getDepLatitude().hashCode());
        result = prime * result + ((getDepArea() == null) ? 0 : getDepArea().hashCode());
        result = prime * result + ((getDepTime() == null) ? 0 : getDepTime().hashCode());
        result = prime * result + ((getDestLongitude() == null) ? 0 : getDestLongitude().hashCode());
        result = prime * result + ((getDestLatitude() == null) ? 0 : getDestLatitude().hashCode());
        result = prime * result + ((getDestArea() == null) ? 0 : getDestArea().hashCode());
        result = prime * result + ((getDestTime() == null) ? 0 : getDestTime().hashCode());
        result = prime * result + ((getBookModel() == null) ? 0 : getBookModel().hashCode());
        result = prime * result + ((getModel() == null) ? 0 : getModel().hashCode());
        result = prime * result + ((getDriveMile() == null) ? 0 : getDriveMile().hashCode());
        result = prime * result + ((getDriveTime() == null) ? 0 : getDriveTime().hashCode());
        result = prime * result + ((getWaitMile() == null) ? 0 : getWaitMile().hashCode());
        result = prime * result + ((getFactPrice() == null) ? 0 : getFactPrice().hashCode());
        result = prime * result + ((getPrice() == null) ? 0 : getPrice().hashCode());
        result = prime * result + ((getCashPrice() == null) ? 0 : getCashPrice().hashCode());
        result = prime * result + ((getLineName() == null) ? 0 : getLineName().hashCode());
        result = prime * result + ((getLinePrice() == null) ? 0 : getLinePrice().hashCode());
        result = prime * result + ((getPosName() == null) ? 0 : getPosName().hashCode());
        result = prime * result + ((getPosPrice() == null) ? 0 : getPosPrice().hashCode());
        result = prime * result + ((getBenfitPrice() == null) ? 0 : getBenfitPrice().hashCode());
        result = prime * result + ((getBookTip() == null) ? 0 : getBookTip().hashCode());
        result = prime * result + ((getPassengerTip() == null) ? 0 : getPassengerTip().hashCode());
        result = prime * result + ((getPeakUpPrice() == null) ? 0 : getPeakUpPrice().hashCode());
        result = prime * result + ((getNightUpPrice() == null) ? 0 : getNightUpPrice().hashCode());
        result = prime * result + ((getFarUpPrice() == null) ? 0 : getFarUpPrice().hashCode());
        result = prime * result + ((getOtherUpPrice() == null) ? 0 : getOtherUpPrice().hashCode());
        result = prime * result + ((getPayState() == null) ? 0 : getPayState().hashCode());
        result = prime * result + ((getPayTime() == null) ? 0 : getPayTime().hashCode());
        result = prime * result + ((getOrderMatchTime() == null) ? 0 : getOrderMatchTime().hashCode());
        result = prime * result + ((getInvoiceStatus() == null) ? 0 : getInvoiceStatus().hashCode());
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
        sb.append(", onArea=").append(onArea);
        sb.append(", driverName=").append(driverName);
        sb.append(", licenseId=").append(licenseId);
        sb.append(", fareType=").append(fareType);
        sb.append(", vehicleNo=").append(vehicleNo);
        sb.append(", bookDepTime=").append(bookDepTime);
        sb.append(", waitTime=").append(waitTime);
        sb.append(", depLongitude=").append(depLongitude);
        sb.append(", depLatitude=").append(depLatitude);
        sb.append(", depArea=").append(depArea);
        sb.append(", depTime=").append(depTime);
        sb.append(", destLongitude=").append(destLongitude);
        sb.append(", destLatitude=").append(destLatitude);
        sb.append(", destArea=").append(destArea);
        sb.append(", destTime=").append(destTime);
        sb.append(", bookModel=").append(bookModel);
        sb.append(", model=").append(model);
        sb.append(", driveMile=").append(driveMile);
        sb.append(", driveTime=").append(driveTime);
        sb.append(", waitMile=").append(waitMile);
        sb.append(", factPrice=").append(factPrice);
        sb.append(", price=").append(price);
        sb.append(", cashPrice=").append(cashPrice);
        sb.append(", lineName=").append(lineName);
        sb.append(", linePrice=").append(linePrice);
        sb.append(", posName=").append(posName);
        sb.append(", posPrice=").append(posPrice);
        sb.append(", benfitPrice=").append(benfitPrice);
        sb.append(", bookTip=").append(bookTip);
        sb.append(", passengerTip=").append(passengerTip);
        sb.append(", peakUpPrice=").append(peakUpPrice);
        sb.append(", nightUpPrice=").append(nightUpPrice);
        sb.append(", farUpPrice=").append(farUpPrice);
        sb.append(", otherUpPrice=").append(otherUpPrice);
        sb.append(", payState=").append(payState);
        sb.append(", payTime=").append(payTime);
        sb.append(", orderMatchTime=").append(orderMatchTime);
        sb.append(", invoiceStatus=").append(invoiceStatus);
        sb.append(", reserved=").append(reserved);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}