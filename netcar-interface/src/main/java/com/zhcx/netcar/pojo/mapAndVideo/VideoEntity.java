package com.zhcx.netcar.pojo.mapAndVideo;//package com.zhcx.netcarbasic.pojo.mapAndVideo;
//
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import jnr.ffi.annotations.In;
//import org.apache.ibatis.annotations.Param;
//
//import java.io.Serializable;
//import java.util.Objects;
//
///**
// * @author buhao
// * @email 1249285896@qq.com
// * @date 2019-06-02 17:20
// */
//public class VideoEntity implements Serializable {
//
//    public Long id;//
//    public Integer channel;//
//    public Integer mediaType;
//    public Integer status;//
//    public String devIdno;//
//    public Long updateTime;//
//    public String vehiIdno;//
//    public String fileETime;//
//    public Long fileSTimeI;//
//    public Integer alarm;//
//    public Integer fileType;//
//    public Long fileSize;//
//    public Long fileSTime;//
//    public Integer alarmType;//
//    public Long fileETimeI;//
//    public Integer alarmParam;//
//    public String filePath;//
//    public String label;//
//
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Integer getChannel() {
//        return channel;
//    }
//
//    public void setChannel(Integer channel) {
//        this.channel = channel;
//    }
//
//    public Integer getMediaType() {
//        return mediaType;
//    }
//
//    public void setMediaType(Integer mediaType) {
//        this.mediaType = mediaType;
//    }
//
//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }
//
//    public String getDevIdno() {
//        return devIdno;
//    }
//
//    public void setDevIdno(String devIdno) {
//        this.devIdno = devIdno;
//    }
//
//    public Long getUpdateTime() {
//        return updateTime;
//    }
//
//    public void setUpdateTime(Long updateTime) {
//        this.updateTime = updateTime;
//    }
//
//    public String getVehiIdno() {
//        return vehiIdno;
//    }
//
//    public void setVehiIdno(String vehiIdno) {
//        this.vehiIdno = vehiIdno;
//    }
//
//    public String getFileETime() {
//        return fileETime;
//    }
//
//    public void setFileETime(String fileETime) {
//        this.fileETime = fileETime;
//    }
//
//    public Long getFileSTimeI() {
//        return fileSTimeI;
//    }
//
//    public void setFileSTimeI(Long fileSTimeI) {
//        this.fileSTimeI = fileSTimeI;
//    }
//
//    public Integer getAlarm() {
//        return alarm;
//    }
//
//    public void setAlarm(Integer alarm) {
//        this.alarm = alarm;
//    }
//
//    public Integer getFileType() {
//        return fileType;
//    }
//
//    public void setFileType(Integer fileType) {
//        this.fileType = fileType;
//    }
//
//    public Long getFileSize() {
//        return fileSize;
//    }
//
//    public void setFileSize(Long fileSize) {
//        this.fileSize = fileSize;
//    }
//
//    public Long getFileSTime() {
//        return fileSTime;
//    }
//
//    public void setFileSTime(Long fileSTime) {
//        this.fileSTime = fileSTime;
//    }
//
//    public Integer getAlarmType() {
//        return alarmType;
//    }
//
//    public void setAlarmType(Integer alarmType) {
//        this.alarmType = alarmType;
//    }
//
//    public Long getFileETimeI() {
//        return fileETimeI;
//    }
//
//    public void setFileETimeI(Long fileETimeI) {
//        this.fileETimeI = fileETimeI;
//    }
//
//    public Integer getAlarmParam() {
//        return alarmParam;
//    }
//
//    public void setAlarmParam(Integer alarmParam) {
//        this.alarmParam = alarmParam;
//    }
//
//    public String getFilePath() {
//        return filePath;
//    }
//
//    public void setFilePath(String filePath) {
//        this.filePath = filePath;
//    }
//
//    public String getLabel() {
//        return label;
//    }
//
//    public void setLabel(String label) {
//        this.label = label;
//    }
//
//    @Override
//    public String toString() {
//        return "VideoEntity{" +
//                "id='" + id + '\'' +
//                ", channel=" + channel +
//                ", mediaType=" + mediaType +
//                ", status=" + status +
//                ", devIdno='" + devIdno + '\'' +
//                ", updateTime=" + updateTime +
//                ", vehiId='" + vehiId + '\'' +
//                ", fileETime='" + fileETime + '\'' +
//                ", fileSTimeI=" + fileSTimeI +
//                ", alarm=" + alarm +
//                ", fileType=" + fileType +
//                ", fileSize=" + fileSize +
//                ", fileSTime=" + fileSTime +
//                ", alarmType=" + alarmType +
//                ", fileETimeI=" + fileETimeI +
//                ", alarmParam=" + alarmParam +
//                ", filePath='" + filePath + '\'' +
//                ", label='" + label + '\'' +
//                '}';
//    }
//}
