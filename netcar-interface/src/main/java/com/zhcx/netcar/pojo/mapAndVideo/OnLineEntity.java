package com.zhcx.netcar.pojo.mapAndVideo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/6/24 0024 13:57
 **/
public class OnLineEntity implements Serializable {


    private static final long serialVersionUID = -1126399123949015545L;
    private String did;
    private String vid;
    private Integer online;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    @Override
    public String toString() {
        return "OnLineEntity{" +
                "did='" + did + '\'' +
                ", vid='" + vid + '\'' +
                ", online=" + online +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnLineEntity that = (OnLineEntity) o;
        return Objects.equals(did, that.did) &&
                Objects.equals(vid, that.vid) &&
                Objects.equals(online, that.online);
    }

    @Override
    public int hashCode() {
        return Objects.hash(did, vid, online);
    }
}
