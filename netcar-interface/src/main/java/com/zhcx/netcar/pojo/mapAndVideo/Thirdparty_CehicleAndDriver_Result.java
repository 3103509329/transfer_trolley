package com.zhcx.netcar.pojo.mapAndVideo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/7/8 0008 14:19
 **/
public class Thirdparty_CehicleAndDriver_Result implements Serializable {

    private static final long serialVersionUID = -776695354704650028L;
    private Integer result;

    private List<Thirdparty_VehicleAndDriver> vehicles;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public List<Thirdparty_VehicleAndDriver> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Thirdparty_VehicleAndDriver> vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public String toString() {
        return "Thirdparty_CehicleAndDriver_Result{" +
                "result=" + result +
                ", vehicles=" + vehicles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Thirdparty_CehicleAndDriver_Result that = (Thirdparty_CehicleAndDriver_Result) o;
        return Objects.equals(result, that.result) &&
                Objects.equals(vehicles, that.vehicles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, vehicles);
    }
}
