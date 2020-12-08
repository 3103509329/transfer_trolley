package com.zhcx.netcar.pojo.mapAndVideo;

import java.io.Serializable;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/5/28 0028 15:52
 **/
public class TrackEntity implements Serializable {

    private static final long serialVersionUID = -5470933878290134255L;
    private String jsession;

    private String devIdno;

    private String vehiIdno;

    private String begintime;

    private String endtime;

    private Integer distance;

    private Integer parkTime;

    private Integer currentPage;

    private Integer pageRecords;

    private Integer toMap;

    @Override
    public String toString() {
        return "TrackEntity{" +
                "jsession='" + jsession + '\'' +
                ", devIdno='" + devIdno + '\'' +
                ", vehiIdno='" + vehiIdno + '\'' +
                ", begintime='" + begintime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", distance=" + distance +
                ", parkTime=" + parkTime +
                ", currentPage=" + currentPage +
                ", pageRecords=" + pageRecords +
                ", toMap=" + toMap +
                '}';
    }

    public String getVehiIdno() {
        return vehiIdno;
    }

    public void setVehiIdno(String vehiIdno) {
        this.vehiIdno = vehiIdno;
    }

    public String getJsession() {
        return jsession;
    }

    public void setJsession(String jsession) {
        this.jsession = jsession;
    }

    public String getDevIdno() {
        return devIdno;
    }

    public void setDevIdno(String devIdno) {
        this.devIdno = devIdno;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getParkTime() {
        return parkTime;
    }

    public void setParkTime(Integer parkTime) {
        this.parkTime = parkTime;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageRecords() {
        return pageRecords;
    }

    public void setPageRecords(Integer pageRecords) {
        this.pageRecords = pageRecords;
    }

    public Integer getToMap() {
        return toMap;
    }

    public void setToMap(Integer toMap) {
        this.toMap = toMap;
    }
}
