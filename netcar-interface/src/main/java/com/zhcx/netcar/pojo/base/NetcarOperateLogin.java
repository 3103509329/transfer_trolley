package com.zhcx.netcar.pojo.base;

import lombok.Data;

import java.io.Serializable;
@Data
public class NetcarOperateLogin extends NetcarBasePojo implements Serializable {
    private String companyId;

    private String licenseId;

    private String vehicleNo;

    private String loginTime;

    private Double longitude;

    private Double latitude;

    private Integer encrypt;

    private String reserved;

    private String type;
    private static final long serialVersionUID = 1L;


}