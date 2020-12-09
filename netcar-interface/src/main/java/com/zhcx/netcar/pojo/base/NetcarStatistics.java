package com.zhcx.netcar.pojo.base;

import java.io.Serializable;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2018/12/6 0006 11:12
 **/
public class NetcarStatistics implements Serializable {

    private Long companyCount;

    private Long driverCount;

    private Long vehicleCount;

    private Long orderCount;

    private String cityId;

    private String cityName;

    public Long getCompanyCount() {
        return companyCount;
    }

    public void setCompanyCount(Long companyCount) {
        this.companyCount = companyCount;
    }

    public Long getDriverCount() {
        return driverCount;
    }

    public void setDriverCount(Long driverCount) {
        this.driverCount = driverCount;
    }

    public Long getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(Long vehicleCount) {
        this.vehicleCount = vehicleCount;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
