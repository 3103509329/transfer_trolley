package com.zhcx.netcar.vo;

import com.zhcx.netcar.annotation.FieldMeta;

import java.io.Serializable;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/7/18 16:52
 * 已取证未运营的车辆信息
 **/
public class VehicleNotOperating implements Serializable {

    private static final long serialVersionUID = -553749240104858408L;
    @FieldMeta(name = "车牌", description = "车牌", index = 0)
    private String branum;

    @FieldMeta(name = "道路运输证号", description = "道路运输证号", index = 1)
    private String trano;

    public String getBranum() {
        return branum;
    }

    public void setBranum(String branum) {
        this.branum = branum;
    }

    public String getTrano() {
        return trano;
    }

    public void setTrano(String trano) {
        this.trano = trano;
    }
}
