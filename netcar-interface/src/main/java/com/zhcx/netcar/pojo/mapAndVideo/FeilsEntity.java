package com.zhcx.netcar.pojo.mapAndVideo;

import java.io.Serializable;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-06-02 18:56
 */
public class FeilsEntity implements Serializable {

    public String DownTaskUrl;
    public String DownUrl;
    public String PlaybackUrl;
    public Integer arm;
    public Integer beg;
    public Integer chn;
    public Integer chnMask;
    public String clientIp;
    public String clientIp2;
    public String clientIp3;
    public Integer clientPort;
    public Integer day;
    public String devIdno;
    public Integer end;
    public Integer fid;
    public String file;
    public String  label;
    public String lanip;
    public Long len;
    public Integer loc;
    public Integer mon;
    public Integer recing;
    public Integer stream;
    public Integer svr;
    public Integer type;
    public Integer year;


    @Override
    public String toString() {
        return "FeilsEntity{" +
                "DownTaskUrl='" + DownTaskUrl + '\'' +
                ", DownUrl='" + DownUrl + '\'' +
                ", PlaybackUrl='" + PlaybackUrl + '\'' +
                ", arm=" + arm +
                ", beg=" + beg +
                ", chn=" + chn +
                ", chnMask=" + chnMask +
                ", clientIp='" + clientIp + '\'' +
                ", clientIp2='" + clientIp2 + '\'' +
                ", clientIp3='" + clientIp3 + '\'' +
                ", clientPort=" + clientPort +
                ", day=" + day +
                ", devIdno='" + devIdno + '\'' +
                ", end=" + end +
                ", fid=" + fid +
                ", file='" + file + '\'' +
                ", label='" + label + '\'' +
                ", lanip='" + lanip + '\'' +
                ", len=" + len +
                ", loc=" + loc +
                ", mon=" + mon +
                ", recing=" + recing +
                ", stream=" + stream +
                ", svr=" + svr +
                ", type=" + type +
                ", year=" + year +
                '}';
    }

    public String getDownTaskUrl() {
        return DownTaskUrl;
    }

    public void setDownTaskUrl(String downTaskUrl) {
        DownTaskUrl = downTaskUrl;
    }

    public String getDownUrl() {
        return DownUrl;
    }

    public void setDownUrl(String downUrl) {
        DownUrl = downUrl;
    }

    public String getPlaybackUrl() {
        return PlaybackUrl;
    }

    public void setPlaybackUrl(String playbackUrl) {
        PlaybackUrl = playbackUrl;
    }

    public Integer getArm() {
        return arm;
    }

    public void setArm(Integer arm) {
        this.arm = arm;
    }

    public Integer getBeg() {
        return beg;
    }

    public void setBeg(Integer beg) {
        this.beg = beg;
    }

    public Integer getChn() {
        return chn;
    }

    public void setChn(Integer chn) {
        this.chn = chn;
    }

    public Integer getChnMask() {
        return chnMask;
    }

    public void setChnMask(Integer chnMask) {
        this.chnMask = chnMask;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getClientIp2() {
        return clientIp2;
    }

    public void setClientIp2(String clientIp2) {
        this.clientIp2 = clientIp2;
    }

    public String getClientIp3() {
        return clientIp3;
    }

    public void setClientIp3(String clientIp3) {
        this.clientIp3 = clientIp3;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    public void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getDevIdno() {
        return devIdno;
    }

    public void setDevIdno(String devIdno) {
        this.devIdno = devIdno;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLanip() {
        return lanip;
    }

    public void setLanip(String lanip) {
        this.lanip = lanip;
    }

    public Long getLen() {
        return len;
    }

    public void setLen(Long len) {
        this.len = len;
    }

    public Integer getLoc() {
        return loc;
    }

    public void setLoc(Integer loc) {
        this.loc = loc;
    }

    public Integer getMon() {
        return mon;
    }

    public void setMon(Integer mon) {
        this.mon = mon;
    }

    public Integer getRecing() {
        return recing;
    }

    public void setRecing(Integer recing) {
        this.recing = recing;
    }

    public Integer getStream() {
        return stream;
    }

    public void setStream(Integer stream) {
        this.stream = stream;
    }

    public Integer getSvr() {
        return svr;
    }

    public void setSvr(Integer svr) {
        this.svr = svr;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
