package com.zhcx.netcar.pojo.mapAndVideo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/7/8 0008 14:09
 **/
public class Thirdparty_VehicleAndDriver implements Serializable {

    private static final long serialVersionUID = 242381745277295497L;
    private String dt;

    private String dn;

    private String nm;

    private String pnm;

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getPnm() {
        return pnm;
    }

    public void setPnm(String pnm) {
        this.pnm = pnm;
    }

    @Override
    public String toString() {
        return "Thirdparty_VehicleAndDriver{" +
                "dt='" + dt + '\'' +
                ", dn='" + dn + '\'' +
                ", nm='" + nm + '\'' +
                ", pnm='" + pnm + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Thirdparty_VehicleAndDriver that = (Thirdparty_VehicleAndDriver) o;
        return Objects.equals(dt, that.dt) &&
                Objects.equals(dn, that.dn) &&
                Objects.equals(nm, that.nm) &&
                Objects.equals(pnm, that.pnm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dt, dn, nm, pnm);
    }
}
