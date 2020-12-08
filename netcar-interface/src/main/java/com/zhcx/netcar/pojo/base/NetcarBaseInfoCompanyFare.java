package com.zhcx.netcar.pojo.base;

import java.io.Serializable;

public class NetcarBaseInfoCompanyFare extends NetcarBasePojo implements Serializable {
    private String companyId;

    private Integer address;

    private String fareType;

    private String fareTypeNote;

    private String fareValidOn;

    private String fareValidOff;

    private Double startFare;

    private Double startMile;

    private Double unitPricePerMile;

    private Double unitPricePerMinute;

    private Double upPrice;

    private Double upPriceStartMile;

    private String morningPeakTimeOn;

    private String morningPeakTimeOff;

    private String eveningPeakTimeOn;

    private String eveningPeakTimeOff;

    private String otherPeakTimeOn;

    private String otherPeakTimeOff;

    private Double peakUnitPrice;

    private Double peakPriceStartMile;

    private Double lowSpeedPricePerMinute;

    private Double nightPricePerMile;

    private Double nightPricePerMinute;

    private Double otherPrice;

    private Integer state;

    private String updateTime;

    private Integer flag;

    private String reserved;

    private static final long serialVersionUID = 1L;

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getAddress() {
        return address;
    }

    public void setAddress(Integer address) {
        this.address = address;
    }

    public String getFareType() {
        return fareType;
    }

    public void setFareType(String fareType) {
        this.fareType = fareType;
    }

    public String getFareTypeNote() {
        return fareTypeNote;
    }

    public void setFareTypeNote(String fareTypeNote) {
        this.fareTypeNote = fareTypeNote;
    }

    public String getFareValidOn() {
        return fareValidOn;
    }

    public void setFareValidOn(String fareValidOn) {
        this.fareValidOn = fareValidOn;
    }

    public String getFareValidOff() {
        return fareValidOff;
    }

    public void setFareValidOff(String fareValidOff) {
        this.fareValidOff = fareValidOff;
    }

    public Double getStartFare() {
        return startFare;
    }

    public void setStartFare(Double startFare) {
        this.startFare = startFare;
    }

    public Double getStartMile() {
        return startMile;
    }

    public void setStartMile(Double startMile) {
        this.startMile = startMile;
    }

    public Double getUnitPricePerMile() {
        return unitPricePerMile;
    }

    public void setUnitPricePerMile(Double unitPricePerMile) {
        this.unitPricePerMile = unitPricePerMile;
    }

    public Double getUnitPricePerMinute() {
        return unitPricePerMinute;
    }

    public void setUnitPricePerMinute(Double unitPricePerMinute) {
        this.unitPricePerMinute = unitPricePerMinute;
    }

    public Double getUpPrice() {
        return upPrice;
    }

    public void setUpPrice(Double upPrice) {
        this.upPrice = upPrice;
    }

    public Double getUpPriceStartMile() {
        return upPriceStartMile;
    }

    public void setUpPriceStartMile(Double upPriceStartMile) {
        this.upPriceStartMile = upPriceStartMile;
    }

    public String getMorningPeakTimeOn() {
        return morningPeakTimeOn;
    }

    public void setMorningPeakTimeOn(String morningPeakTimeOn) {
        this.morningPeakTimeOn = morningPeakTimeOn;
    }

    public String getMorningPeakTimeOff() {
        return morningPeakTimeOff;
    }

    public void setMorningPeakTimeOff(String morningPeakTimeOff) {
        this.morningPeakTimeOff = morningPeakTimeOff;
    }

    public String getEveningPeakTimeOn() {
        return eveningPeakTimeOn;
    }

    public void setEveningPeakTimeOn(String eveningPeakTimeOn) {
        this.eveningPeakTimeOn = eveningPeakTimeOn;
    }

    public String getEveningPeakTimeOff() {
        return eveningPeakTimeOff;
    }

    public void setEveningPeakTimeOff(String eveningPeakTimeOff) {
        this.eveningPeakTimeOff = eveningPeakTimeOff;
    }

    public String getOtherPeakTimeOn() {
        return otherPeakTimeOn;
    }

    public void setOtherPeakTimeOn(String otherPeakTimeOn) {
        this.otherPeakTimeOn = otherPeakTimeOn;
    }

    public String getOtherPeakTimeOff() {
        return otherPeakTimeOff;
    }

    public void setOtherPeakTimeOff(String otherPeakTimeOff) {
        this.otherPeakTimeOff = otherPeakTimeOff;
    }

    public Double getPeakUnitPrice() {
        return peakUnitPrice;
    }

    public void setPeakUnitPrice(Double peakUnitPrice) {
        this.peakUnitPrice = peakUnitPrice;
    }

    public Double getPeakPriceStartMile() {
        return peakPriceStartMile;
    }

    public void setPeakPriceStartMile(Double peakPriceStartMile) {
        this.peakPriceStartMile = peakPriceStartMile;
    }

    public Double getLowSpeedPricePerMinute() {
        return lowSpeedPricePerMinute;
    }

    public void setLowSpeedPricePerMinute(Double lowSpeedPricePerMinute) {
        this.lowSpeedPricePerMinute = lowSpeedPricePerMinute;
    }

    public Double getNightPricePerMile() {
        return nightPricePerMile;
    }

    public void setNightPricePerMile(Double nightPricePerMile) {
        this.nightPricePerMile = nightPricePerMile;
    }

    public Double getNightPricePerMinute() {
        return nightPricePerMinute;
    }

    public void setNightPricePerMinute(Double nightPricePerMinute) {
        this.nightPricePerMinute = nightPricePerMinute;
    }

    public Double getOtherPrice() {
        return otherPrice;
    }

    public void setOtherPrice(Double otherPrice) {
        this.otherPrice = otherPrice;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
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
        NetcarBaseInfoCompanyFare other = (NetcarBaseInfoCompanyFare) that;
        return (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getFareType() == null ? other.getFareType() == null : this.getFareType().equals(other.getFareType()))
            && (this.getFareTypeNote() == null ? other.getFareTypeNote() == null : this.getFareTypeNote().equals(other.getFareTypeNote()))
            && (this.getFareValidOn() == null ? other.getFareValidOn() == null : this.getFareValidOn().equals(other.getFareValidOn()))
            && (this.getFareValidOff() == null ? other.getFareValidOff() == null : this.getFareValidOff().equals(other.getFareValidOff()))
            && (this.getStartFare() == null ? other.getStartFare() == null : this.getStartFare().equals(other.getStartFare()))
            && (this.getStartMile() == null ? other.getStartMile() == null : this.getStartMile().equals(other.getStartMile()))
            && (this.getUnitPricePerMile() == null ? other.getUnitPricePerMile() == null : this.getUnitPricePerMile().equals(other.getUnitPricePerMile()))
            && (this.getUnitPricePerMinute() == null ? other.getUnitPricePerMinute() == null : this.getUnitPricePerMinute().equals(other.getUnitPricePerMinute()))
            && (this.getUpPrice() == null ? other.getUpPrice() == null : this.getUpPrice().equals(other.getUpPrice()))
            && (this.getUpPriceStartMile() == null ? other.getUpPriceStartMile() == null : this.getUpPriceStartMile().equals(other.getUpPriceStartMile()))
            && (this.getMorningPeakTimeOn() == null ? other.getMorningPeakTimeOn() == null : this.getMorningPeakTimeOn().equals(other.getMorningPeakTimeOn()))
            && (this.getMorningPeakTimeOff() == null ? other.getMorningPeakTimeOff() == null : this.getMorningPeakTimeOff().equals(other.getMorningPeakTimeOff()))
            && (this.getEveningPeakTimeOn() == null ? other.getEveningPeakTimeOn() == null : this.getEveningPeakTimeOn().equals(other.getEveningPeakTimeOn()))
            && (this.getEveningPeakTimeOff() == null ? other.getEveningPeakTimeOff() == null : this.getEveningPeakTimeOff().equals(other.getEveningPeakTimeOff()))
            && (this.getOtherPeakTimeOn() == null ? other.getOtherPeakTimeOn() == null : this.getOtherPeakTimeOn().equals(other.getOtherPeakTimeOn()))
            && (this.getOtherPeakTimeOff() == null ? other.getOtherPeakTimeOff() == null : this.getOtherPeakTimeOff().equals(other.getOtherPeakTimeOff()))
            && (this.getPeakUnitPrice() == null ? other.getPeakUnitPrice() == null : this.getPeakUnitPrice().equals(other.getPeakUnitPrice()))
            && (this.getPeakPriceStartMile() == null ? other.getPeakPriceStartMile() == null : this.getPeakPriceStartMile().equals(other.getPeakPriceStartMile()))
            && (this.getLowSpeedPricePerMinute() == null ? other.getLowSpeedPricePerMinute() == null : this.getLowSpeedPricePerMinute().equals(other.getLowSpeedPricePerMinute()))
            && (this.getNightPricePerMile() == null ? other.getNightPricePerMile() == null : this.getNightPricePerMile().equals(other.getNightPricePerMile()))
            && (this.getNightPricePerMinute() == null ? other.getNightPricePerMinute() == null : this.getNightPricePerMinute().equals(other.getNightPricePerMinute()))
            && (this.getOtherPrice() == null ? other.getOtherPrice() == null : this.getOtherPrice().equals(other.getOtherPrice()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getFlag() == null ? other.getFlag() == null : this.getFlag().equals(other.getFlag()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getFareType() == null) ? 0 : getFareType().hashCode());
        result = prime * result + ((getFareTypeNote() == null) ? 0 : getFareTypeNote().hashCode());
        result = prime * result + ((getFareValidOn() == null) ? 0 : getFareValidOn().hashCode());
        result = prime * result + ((getFareValidOff() == null) ? 0 : getFareValidOff().hashCode());
        result = prime * result + ((getStartFare() == null) ? 0 : getStartFare().hashCode());
        result = prime * result + ((getStartMile() == null) ? 0 : getStartMile().hashCode());
        result = prime * result + ((getUnitPricePerMile() == null) ? 0 : getUnitPricePerMile().hashCode());
        result = prime * result + ((getUnitPricePerMinute() == null) ? 0 : getUnitPricePerMinute().hashCode());
        result = prime * result + ((getUpPrice() == null) ? 0 : getUpPrice().hashCode());
        result = prime * result + ((getUpPriceStartMile() == null) ? 0 : getUpPriceStartMile().hashCode());
        result = prime * result + ((getMorningPeakTimeOn() == null) ? 0 : getMorningPeakTimeOn().hashCode());
        result = prime * result + ((getMorningPeakTimeOff() == null) ? 0 : getMorningPeakTimeOff().hashCode());
        result = prime * result + ((getEveningPeakTimeOn() == null) ? 0 : getEveningPeakTimeOn().hashCode());
        result = prime * result + ((getEveningPeakTimeOff() == null) ? 0 : getEveningPeakTimeOff().hashCode());
        result = prime * result + ((getOtherPeakTimeOn() == null) ? 0 : getOtherPeakTimeOn().hashCode());
        result = prime * result + ((getOtherPeakTimeOff() == null) ? 0 : getOtherPeakTimeOff().hashCode());
        result = prime * result + ((getPeakUnitPrice() == null) ? 0 : getPeakUnitPrice().hashCode());
        result = prime * result + ((getPeakPriceStartMile() == null) ? 0 : getPeakPriceStartMile().hashCode());
        result = prime * result + ((getLowSpeedPricePerMinute() == null) ? 0 : getLowSpeedPricePerMinute().hashCode());
        result = prime * result + ((getNightPricePerMile() == null) ? 0 : getNightPricePerMile().hashCode());
        result = prime * result + ((getNightPricePerMinute() == null) ? 0 : getNightPricePerMinute().hashCode());
        result = prime * result + ((getOtherPrice() == null) ? 0 : getOtherPrice().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getFlag() == null) ? 0 : getFlag().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", companyId=").append(companyId);
        sb.append(", address=").append(address);
        sb.append(", fareType=").append(fareType);
        sb.append(", fareTypeNote=").append(fareTypeNote);
        sb.append(", fareValidOn=").append(fareValidOn);
        sb.append(", fareValidOff=").append(fareValidOff);
        sb.append(", startFare=").append(startFare);
        sb.append(", startMile=").append(startMile);
        sb.append(", unitPricePerMile=").append(unitPricePerMile);
        sb.append(", unitPricePerMinute=").append(unitPricePerMinute);
        sb.append(", upPrice=").append(upPrice);
        sb.append(", upPriceStartMile=").append(upPriceStartMile);
        sb.append(", morningPeakTimeOn=").append(morningPeakTimeOn);
        sb.append(", morningPeakTimeOff=").append(morningPeakTimeOff);
        sb.append(", eveningPeakTimeOn=").append(eveningPeakTimeOn);
        sb.append(", eveningPeakTimeOff=").append(eveningPeakTimeOff);
        sb.append(", otherPeakTimeOn=").append(otherPeakTimeOn);
        sb.append(", otherPeakTimeOff=").append(otherPeakTimeOff);
        sb.append(", peakUnitPrice=").append(peakUnitPrice);
        sb.append(", peakPriceStartMile=").append(peakPriceStartMile);
        sb.append(", lowSpeedPricePerMinute=").append(lowSpeedPricePerMinute);
        sb.append(", nightPricePerMile=").append(nightPricePerMile);
        sb.append(", nightPricePerMinute=").append(nightPricePerMinute);
        sb.append(", otherPrice=").append(otherPrice);
        sb.append(", state=").append(state);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", flag=").append(flag);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}