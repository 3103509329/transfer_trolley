package com.zhcx.netcar.pojo.base;

import java.io.Serializable;

public class NetcarServiceStatus implements Serializable {

    //服务id
    private String id;

    //服务名
    private String serviceName;

    //服务状态(up/down)
    private String status;

    //最后成功时间/异常时间
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "NetcarServiceStatus{" +
                "id='" + id + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", status='" + status + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
