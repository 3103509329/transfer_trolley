package com.zhcx.netcar.constant;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/5/31 0031 9:29
 **/
public enum ThirdPartyURL {


    //旧版本第三方对接（一样）

//    MAP_CURRENT(0,"http://121.43.107.22:8080/StandardApiAction_vehicleStatus.action"),//实时定位
//    MAP_TRACK(1,"http://121.43.107.22:8080/StandardApiAction_queryTrackDetail.action"),//历史轨迹
//    VIDEO_CURRENT(2,""),//实时是视频
//    VICEO_LIST(3,"http://121.43.107.22:8080/StandardApiAction_getVideoFileInfo.action"),//视频历史文件获取
//    EQUIPMENT_NUMBER(4,"http://121.43.107.22:8080/StandardApiAction_getDeviceByVehicle.action"),//获取设备编号
//    LOGIN(5,"http://121.43.107.22:8080/StandardApiAction_login.action"),//登陆
//    ONLINE(6,"http://121.43.107.22:8080/StandardApiAction_getDeviceOlStatus.action"),//在离线状态
//    VEHICLEANDDRIVER(7,"http://121.43.107.22:8080/StandardApiAction_queryUserVehicle.action"),
//    STATE(8,"http://121.43.107.22:8080/StandardApiAction_getDeviceStatus.action");//设备状态：包含速度，定位，在离线状态

    LOGIN(0,"/login"),
    LOGOUT(1,"/logout"),
    STATE(2,"/vehicle/deviceId/status"),
    VEHICLE(3,"/vehicle/list"),
    DEVICEID(4,"/vehicle/deviceId"),
    REALLOCATION(5,"/track/history"),
    HISTORYLOCATION(6,"/track/real"),
    REALVIDEO(7,"/video/real"),
    HISTORYVICEO(8,"/video/history"),
    INSTRUCTPICTURE(9,"/picture/real"),
    PICTURELIST(11,"/picture/history/list"),
    INSTURCTTTS(12,"/tts");

    private final int code;

    private final String desc;

    ThirdPartyURL(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
