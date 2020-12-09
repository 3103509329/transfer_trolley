package com.zhcx.netcar.pojo.mapAndVideo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/6/1 0001 10:39
 **/
public class MapHistory implements Serializable {

    private static final long serialVersionUID = 2653333704221202352L;

    private String id;
    private Long lng;
    private Long lat;
    private Integer ft;
    private Integer sp;
    private Integer ol;
    private String gt;
    private Integer pt;
    private Integer dt;
    private Integer ac;
    private Integer fdt;
    private Integer net;
    private String gw;
    private Long s1;
    private Integer s2;
    private Integer s3;
    private Integer s4;
    private Integer t1;
    private Integer t2;
    private Integer t3;
    private Integer t4;
    private Integer hx;
    private String mlng;
    private String mlat;
    private Integer pk;
    private Long lc;
    private Integer yl;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapHistory that = (MapHistory) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(lng, that.lng) &&
                Objects.equals(lat, that.lat) &&
                Objects.equals(ft, that.ft) &&
                Objects.equals(sp, that.sp) &&
                Objects.equals(ol, that.ol) &&
                Objects.equals(gt, that.gt) &&
                Objects.equals(pt, that.pt) &&
                Objects.equals(dt, that.dt) &&
                Objects.equals(ac, that.ac) &&
                Objects.equals(fdt, that.fdt) &&
                Objects.equals(net, that.net) &&
                Objects.equals(gw, that.gw) &&
                Objects.equals(s1, that.s1) &&
                Objects.equals(s2, that.s2) &&
                Objects.equals(s3, that.s3) &&
                Objects.equals(s4, that.s4) &&
                Objects.equals(t1, that.t1) &&
                Objects.equals(t2, that.t2) &&
                Objects.equals(t3, that.t3) &&
                Objects.equals(t4, that.t4) &&
                Objects.equals(hx, that.hx) &&
                Objects.equals(mlng, that.mlng) &&
                Objects.equals(mlat, that.mlat) &&
                Objects.equals(pk, that.pk) &&
                Objects.equals(lc, that.lc) &&
                Objects.equals(yl, that.yl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lng, lat, ft, sp, ol, gt, pt, dt, ac, fdt, net, gw, s1, s2, s3, s4, t1, t2, t3, t4, hx, mlng, mlat, pk, lc, yl);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getLng() {
        return lng;
    }

    public void setLng(Long lng) {
        this.lng = lng;
    }

    public Long getLat() {
        return lat;
    }

    public void setLat(Long lat) {
        this.lat = lat;
    }

    public Integer getFt() {
        return ft;
    }

    public void setFt(Integer ft) {
        this.ft = ft;
    }

    public Integer getSp() {
        return sp;
    }

    public void setSp(Integer sp) {
        this.sp = sp;
    }

    public Integer getOl() {
        return ol;
    }

    public void setOl(Integer ol) {
        this.ol = ol;
    }

    public String getGt() {
        return gt;
    }

    public void setGt(String gt) {
        this.gt = gt;
    }

    public Integer getPt() {
        return pt;
    }

    public void setPt(Integer pt) {
        this.pt = pt;
    }

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public Integer getAc() {
        return ac;
    }

    public void setAc(Integer ac) {
        this.ac = ac;
    }

    public Integer getFdt() {
        return fdt;
    }

    public void setFdt(Integer fdt) {
        this.fdt = fdt;
    }

    public Integer getNet() {
        return net;
    }

    public void setNet(Integer net) {
        this.net = net;
    }

    public String getGw() {
        return gw;
    }

    public void setGw(String gw) {
        this.gw = gw;
    }

    public Long getS1() {
        return s1;
    }

    public void setS1(Long s1) {
        this.s1 = s1;
    }

    public Integer getS2() {
        return s2;
    }

    public void setS2(Integer s2) {
        this.s2 = s2;
    }

    public Integer getS3() {
        return s3;
    }

    public void setS3(Integer s3) {
        this.s3 = s3;
    }

    public Integer getS4() {
        return s4;
    }

    public void setS4(Integer s4) {
        this.s4 = s4;
    }

    public Integer getT1() {
        return t1;
    }

    public void setT1(Integer t1) {
        this.t1 = t1;
    }

    public Integer getT2() {
        return t2;
    }

    public void setT2(Integer t2) {
        this.t2 = t2;
    }

    public Integer getT3() {
        return t3;
    }

    public void setT3(Integer t3) {
        this.t3 = t3;
    }

    public Integer getT4() {
        return t4;
    }

    public void setT4(Integer t4) {
        this.t4 = t4;
    }

    public Integer getHx() {
        return hx;
    }

    public void setHx(Integer hx) {
        this.hx = hx;
    }

    public String getMlng() {
        return mlng;
    }

    public void setMlng(String mlng) {
        this.mlng = mlng;
    }

    public String getMlat() {
        return mlat;
    }

    public void setMlat(String mlat) {
        this.mlat = mlat;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public Long getLc() {
        return lc;
    }

    public void setLc(Long lc) {
        this.lc = lc;
    }

    public Integer getYl() {
        return yl;
    }

    public void setYl(Integer yl) {
        this.yl = yl;
    }
}
