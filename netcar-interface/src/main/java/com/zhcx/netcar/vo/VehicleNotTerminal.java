package com.zhcx.netcar.vo;

import com.zhcx.netcar.annotation.FieldMeta;

import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/7/18 16:52
 * 无车载终端的车辆
 **/
public class VehicleNotTerminal implements Serializable {

    private static final long serialVersionUID = 4738664766585901477L;

    @FieldMeta(name = "车牌", description = "车牌", index = 0)
    private String branum;

    @FieldMeta(name = "卫星定位设备安装日期", description = "卫星定位设备安装日期", index = 1)
    private String gpsInstallDate;

    @FieldMeta(name = "卫星定位装置品牌", description = "卫星定位装置品牌", index = 2)
    private String gpsImei;

    @FieldMeta(name = "卫星定位装置型号", description = "卫星定位装置型号", index = 3)
    private String gpsModel;

    @FieldMeta(name = "卫星定位装置IMEI号", description = "卫星定位装置IMEI号", index = 4)
    private String gpsBrand;

    public String getBranum() {
        return branum;
    }

    public void setBranum(String branum) {
        this.branum = branum;
    }

    public String getGpsInstallDate() {
        return gpsInstallDate;
    }

    public void setGpsInstallDate(String gpsInstallDate) {
        this.gpsInstallDate = gpsInstallDate;
    }

    public String getGpsImei() {
        return gpsImei;
    }

    public void setGpsImei(String gpsImei) {
        this.gpsImei = gpsImei;
    }

    public String getGpsModel() {
        return gpsModel;
    }

    public void setGpsModel(String gpsModel) {
        this.gpsModel = gpsModel;
    }

    public String getGpsBrand() {
        return gpsBrand;
    }

    public void setGpsBrand(String gpsBrand) {
        this.gpsBrand = gpsBrand;
    }
}
