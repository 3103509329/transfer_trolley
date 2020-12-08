package com.zhcx.netcar.pojo.app;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class NetcarAppTrafficAssessment implements Serializable {
    private Long uuid;

    private String ticketNo;

    private String content;

    private String location;

    private String item;

    private String remark;

    private String tool;

    private String creator;

    private String branum;

    private String checkTime;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private String clitname;

    private static final long serialVersionUID = 1L;

}