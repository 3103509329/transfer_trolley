package com.zhcx.netcar.pojo.mapAndVideo;

import javafx.scene.control.Pagination;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/6/1 0001 11:25
 **/
public class MapRsult implements Serializable {

    private static final long serialVersionUID = 4105589091184334792L;
    public Integer result;
    public transient List<MapHistory> tracks;
    public Pagination pagination;
    public List<CurrentEntity> infos;

    @Override
    public String toString() {
        return "MapRsult{" +
                "result=" + result +
                ", tracks=" + tracks +
                ", pagination=" + pagination +
                ", infos=" + infos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapRsult mapRsult = (MapRsult) o;
        return Objects.equals(result, mapRsult.result) &&
                Objects.equals(tracks, mapRsult.tracks) &&
                Objects.equals(pagination, mapRsult.pagination) &&
                Objects.equals(infos, mapRsult.infos);
    }

    @Override
    public int hashCode() {

        return Objects.hash(result, tracks, pagination, infos);
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public List<MapHistory> getTracks() {
        return tracks;
    }

    public void setTracks(List<MapHistory> tracks) {
        this.tracks = tracks;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<CurrentEntity> getInfos() {
        return infos;
    }

    public void setInfos(List<CurrentEntity> infos) {
        this.infos = infos;
    }
}
