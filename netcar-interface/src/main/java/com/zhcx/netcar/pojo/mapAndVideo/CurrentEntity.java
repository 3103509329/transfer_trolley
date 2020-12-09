package com.zhcx.netcar.pojo.mapAndVideo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-06-02 10:36
 */
public class CurrentEntity implements Serializable {

    private static final long serialVersionUID = 8787748698889209215L;
    private String vi;//设备编号

    private String vid;//车牌号

    private Integer sp;//速度

    private String mlng;//地图经度

    private String mlat;//地图纬度

    private String ps;

    private Integer ol;//在离线状态

    private Integer hx;//方向

    private String dn;//驾驶员姓名

    private String dt;//驾驶员电话

    private Integer online;//在离线状态

    private String pos;

    private String jd;

    private String wd;

    private String tm;

    private String gt;

    public String getVi() {
        return vi;
    }

    public void setVi(String vi) {
        this.vi = vi;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public Integer getSp() {
        return sp;
    }

    public void setSp(Integer sp) {
        this.sp = sp;
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

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    public Integer getOl() {
        return ol;
    }

    public void setOl(Integer ol) {
        this.ol = ol;
    }

    public Integer getHx() {
        return hx;
    }

    public void setHx(Integer hx) {
        this.hx = hx;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getJd() {
        return jd;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public String getTm() {
        return tm;
    }

    public void setTm(String tm) {
        this.tm = tm;
    }

    public String getGt() {
        return gt;
    }

    public void setGt(String gt) {
        this.gt = gt;
    }

    @Override
    public String toString() {
        return "CurrentEntity{" +
                "vi='" + vi + '\'' +
                ", vid='" + vid + '\'' +
                ", sp=" + sp +
                ", mlng='" + mlng + '\'' +
                ", mlat='" + mlat + '\'' +
                ", ps='" + ps + '\'' +
                ", ol=" + ol +
                ", hx=" + hx +
                ", dn='" + dn + '\'' +
                ", dt='" + dt + '\'' +
                ", online=" + online +
                ", pos='" + pos + '\'' +
                ", jd='" + jd + '\'' +
                ", wd='" + wd + '\'' +
                ", tm='" + tm + '\'' +
                ", gt='" + gt + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrentEntity that = (CurrentEntity) o;
        return Objects.equals(vi, that.vi) &&
                Objects.equals(vid, that.vid) &&
                Objects.equals(sp, that.sp) &&
                Objects.equals(mlng, that.mlng) &&
                Objects.equals(mlat, that.mlat) &&
                Objects.equals(ps, that.ps) &&
                Objects.equals(ol, that.ol) &&
                Objects.equals(hx, that.hx) &&
                Objects.equals(dn, that.dn) &&
                Objects.equals(dt, that.dt) &&
                Objects.equals(online, that.online) &&
                Objects.equals(pos, that.pos) &&
                Objects.equals(jd, that.jd) &&
                Objects.equals(wd, that.wd) &&
                Objects.equals(tm, that.tm) &&
                Objects.equals(gt, that.gt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vi, vid, sp, mlng, mlat, ps, ol, hx, dn, dt, online, pos, jd, wd, tm, gt);
    }
}
