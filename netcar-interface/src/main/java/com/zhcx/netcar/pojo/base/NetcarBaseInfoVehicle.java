package com.zhcx.netcar.pojo.base;

import java.io.Serializable;
import java.util.Objects;

public class NetcarBaseInfoVehicle extends NetcarBasePojo implements Serializable {
    private String companyId;

    private String vehicleNo;

    private Integer address;

    private String plateColor;

    private Integer seats;

    private String brand;

    private String model;

    private String vehicleType;

    private String ownerName;

    private String vehicleColor;

    private String engineId;

    private String vin;

    private String certifyDateA;

    private String fuelType;

    private String engineDisplace;

    private String photoId;

    private String certificate;

    private String transAgency;

    private String transArea;

    private String transDateStart;

    private String transDateStop;

    private String certifyDateB;

    private String fixState;

    private String nextFixDate;

    private String checkState;

    private String feePrintId;

    private String gpsBrand;

    private String gpsModel;

    private String gpsImei;

    private String gpsInstallDate;

    private String registerDate;

    private Integer commercialType;

    private String fareType;

    private Integer state;

    private Integer flag;

    private String updateTime;

    private String reserved;



    /**
     * 添加属性 用于前端显示树形结构
     */
    private String tag;
    /**
     * 添加属性 用于前端显示树形结构
     */
    private String label;

    private Integer dataSourcesType;

    private static final long serialVersionUID = 1L;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getDataSourcesType() {
        return dataSourcesType;
    }

    public void setDataSourcesType(Integer dataSourcesType) {
        this.dataSourcesType = dataSourcesType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public Integer getAddress() {
        return address;
    }

    public void setAddress(Integer address) {
        this.address = address;
    }

    public String getPlateColor() {
        return plateColor;
    }

    public void setPlateColor(String plateColor) {
        this.plateColor = plateColor;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getEngineId() {
        return engineId;
    }

    public void setEngineId(String engineId) {
        this.engineId = engineId;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getCertifyDateA() {
        return certifyDateA;
    }

    public void setCertifyDateA(String certifyDateA) {
        this.certifyDateA = certifyDateA;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getEngineDisplace() {
        return engineDisplace;
    }

    public void setEngineDisplace(String engineDisplace) {
        this.engineDisplace = engineDisplace;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getTransAgency() {
        return transAgency;
    }

    public void setTransAgency(String transAgency) {
        this.transAgency = transAgency;
    }

    public String getTransArea() {
        return transArea;
    }

    public void setTransArea(String transArea) {
        this.transArea = transArea;
    }

    public String getTransDateStart() {
        return transDateStart;
    }

    public void setTransDateStart(String transDateStart) {
        this.transDateStart = transDateStart;
    }

    public String getTransDateStop() {
        return transDateStop;
    }

    public void setTransDateStop(String transDateStop) {
        this.transDateStop = transDateStop;
    }

    public String getCertifyDateB() {
        return certifyDateB;
    }

    public void setCertifyDateB(String certifyDateB) {
        this.certifyDateB = certifyDateB;
    }

    public String getFixState() {
        return fixState;
    }

    public void setFixState(String fixState) {
        this.fixState = fixState;
    }

    public String getNextFixDate() {
        return nextFixDate;
    }

    public void setNextFixDate(String nextFixDate) {
        this.nextFixDate = nextFixDate;
    }

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public String getFeePrintId() {
        return feePrintId;
    }

    public void setFeePrintId(String feePrintId) {
        this.feePrintId = feePrintId;
    }

    public String getGpsBrand() {
        return gpsBrand;
    }

    public void setGpsBrand(String gpsBrand) {
        this.gpsBrand = gpsBrand;
    }

    public String getGpsModel() {
        return gpsModel;
    }

    public void setGpsModel(String gpsModel) {
        this.gpsModel = gpsModel;
    }

    public String getGpsImei() {
        return gpsImei;
    }

    public void setGpsImei(String gpsImei) {
        this.gpsImei = gpsImei;
    }

    public String getGpsInstallDate() {
        return gpsInstallDate;
    }

    public void setGpsInstallDate(String gpsInstallDate) {
        this.gpsInstallDate = gpsInstallDate;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public Integer getCommercialType() {
        return commercialType;
    }

    public void setCommercialType(Integer commercialType) {
        this.commercialType = commercialType;
    }

    public String getFareType() {
        return fareType;
    }

    public void setFareType(String fareType) {
        this.fareType = fareType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NetcarBaseInfoVehicle that = (NetcarBaseInfoVehicle) o;
        return Objects.equals(companyId, that.companyId) &&
                Objects.equals(vehicleNo, that.vehicleNo) &&
                Objects.equals(address, that.address) &&
                Objects.equals(plateColor, that.plateColor) &&
                Objects.equals(seats, that.seats) &&
                Objects.equals(brand, that.brand) &&
                Objects.equals(model, that.model) &&
                Objects.equals(vehicleType, that.vehicleType) &&
                Objects.equals(ownerName, that.ownerName) &&
                Objects.equals(vehicleColor, that.vehicleColor) &&
                Objects.equals(engineId, that.engineId) &&
                Objects.equals(vin, that.vin) &&
                Objects.equals(certifyDateA, that.certifyDateA) &&
                Objects.equals(fuelType, that.fuelType) &&
                Objects.equals(engineDisplace, that.engineDisplace) &&
                Objects.equals(photoId, that.photoId) &&
                Objects.equals(certificate, that.certificate) &&
                Objects.equals(transAgency, that.transAgency) &&
                Objects.equals(transArea, that.transArea) &&
                Objects.equals(transDateStart, that.transDateStart) &&
                Objects.equals(transDateStop, that.transDateStop) &&
                Objects.equals(certifyDateB, that.certifyDateB) &&
                Objects.equals(fixState, that.fixState) &&
                Objects.equals(nextFixDate, that.nextFixDate) &&
                Objects.equals(checkState, that.checkState) &&
                Objects.equals(feePrintId, that.feePrintId) &&
                Objects.equals(gpsBrand, that.gpsBrand) &&
                Objects.equals(gpsModel, that.gpsModel) &&
                Objects.equals(gpsImei, that.gpsImei) &&
                Objects.equals(gpsInstallDate, that.gpsInstallDate) &&
                Objects.equals(registerDate, that.registerDate) &&
                Objects.equals(commercialType, that.commercialType) &&
                Objects.equals(fareType, that.fareType) &&
                Objects.equals(state, that.state) &&
                Objects.equals(flag, that.flag) &&
                Objects.equals(updateTime, that.updateTime) &&
                Objects.equals(reserved, that.reserved) &&
                Objects.equals(tag, that.tag) &&
                Objects.equals(label, that.label) &&
                Objects.equals(dataSourcesType, that.dataSourcesType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, vehicleNo, address, plateColor, seats, brand, model, vehicleType, ownerName, vehicleColor, engineId, vin, certifyDateA, fuelType, engineDisplace, photoId, certificate, transAgency, transArea, transDateStart, transDateStop, certifyDateB, fixState, nextFixDate, checkState, feePrintId, gpsBrand, gpsModel, gpsImei, gpsInstallDate, registerDate, commercialType, fareType, state, flag, updateTime, reserved, tag, label, dataSourcesType);
    }

    @Override
    public String toString() {
        return "NetcarBaseInfoVehicle{" +
                "companyId='" + companyId + '\'' +
                ", vehicleNo='" + vehicleNo + '\'' +
                ", address=" + address +
                ", plateColor='" + plateColor + '\'' +
                ", seats=" + seats +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", vehicleColor='" + vehicleColor + '\'' +
                ", engineId='" + engineId + '\'' +
                ", vin='" + vin + '\'' +
                ", certifyDateA='" + certifyDateA + '\'' +
                ", fuelType='" + fuelType + '\'' +
                ", engineDisplace='" + engineDisplace + '\'' +
                ", photoId='" + photoId + '\'' +
                ", certificate='" + certificate + '\'' +
                ", transAgency='" + transAgency + '\'' +
                ", transArea='" + transArea + '\'' +
                ", transDateStart='" + transDateStart + '\'' +
                ", transDateStop='" + transDateStop + '\'' +
                ", certifyDateB='" + certifyDateB + '\'' +
                ", fixState='" + fixState + '\'' +
                ", nextFixDate='" + nextFixDate + '\'' +
                ", checkState='" + checkState + '\'' +
                ", feePrintId='" + feePrintId + '\'' +
                ", gpsBrand='" + gpsBrand + '\'' +
                ", gpsModel='" + gpsModel + '\'' +
                ", gpsImei='" + gpsImei + '\'' +
                ", gpsInstallDate='" + gpsInstallDate + '\'' +
                ", registerDate='" + registerDate + '\'' +
                ", commercialType=" + commercialType +
                ", fareType='" + fareType + '\'' +
                ", state=" + state +
                ", flag=" + flag +
                ", updateTime='" + updateTime + '\'' +
                ", reserved='" + reserved + '\'' +
                ", tag='" + tag + '\'' +
                ", label='" + label + '\'' +
                ", dataSourcesType=" + dataSourcesType +
                '}';
    }
}