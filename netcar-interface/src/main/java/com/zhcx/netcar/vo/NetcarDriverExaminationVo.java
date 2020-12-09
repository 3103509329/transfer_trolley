package com.zhcx.netcar.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/2/25 13:36
 **/
public class NetcarDriverExaminationVo implements Serializable {

    private static final long serialVersionUID = -1145319340563802478L;

    private Long uuid;

    private String username;

    private String phone;

    private String driverIdCard;

    /**
     * 预约时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date bookedTime;

    private String reason;

    private Integer flag;

    private String examAddress;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date examTime;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDriverIdCard() {
        return driverIdCard;
    }

    public void setDriverIdCard(String driverIdCard) {
        this.driverIdCard = driverIdCard;
    }

    public Date getBookedTime() {
        return bookedTime;
    }

    public void setBookedTime(Date bookedTime) {
        this.bookedTime = bookedTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getExamAddress() {
        return examAddress;
    }

    public void setExamAddress(String examAddress) {
        this.examAddress = examAddress;
    }

    public Date getExamTime() {
        return examTime;
    }

    public void setExamTime(Date examTime) {
        this.examTime = examTime;
    }
}
