package com.zhcx.netcar.pojo.yuzheng;

import lombok.Data;

import java.io.Serializable;

@Data
public class YunZhengDriver implements Serializable {
    private static final long serialVersionUID = 3143284161049748392L;
    private String cardno;

    private String name;

    private String sex;

    private String nation;

    private String address;

    private String birthday;

    private String perdritype;

    private String beworscope;

    private String startdate;

    private String enddate;

    private String gradate;

    private String time;

    private Integer flage;

    private String busiregnumber;

    private String dristadate;

    private String certificateNo;

    private String networkCarIssueOrganization;

    private String networkCarIssueDate;

    private String driverPhone;

    private String clitname;

    private String companyName;

}