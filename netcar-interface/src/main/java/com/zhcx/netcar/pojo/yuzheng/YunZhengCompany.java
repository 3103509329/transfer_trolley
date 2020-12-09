package com.zhcx.netcar.pojo.yuzheng;

import lombok.Data;

import java.io.Serializable;

@Data
public class YunZhengCompany implements Serializable {
    private Long uuid;

    private String clitname;

    private String bnscope;

    private String address;

    private String personLiable;

    private String phone;

    private String businessLicense;

    private String startDate;

    private String endDate;

    private String busiregnumber;

    private String registeredCapital;

    private String establishmentTime;

    private String time;

    private Integer flage;

    private static final long serialVersionUID = 1L;

    private String companyId;

}