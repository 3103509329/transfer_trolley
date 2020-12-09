package com.zhcx.netcar.pojo.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class NetcarPermitDriverInfo implements Serializable {
    private Long uuid;

    private Long corpId;

    private String companyName;

    private String handler;

    private String handlerPhone;

    private Integer address;

    private String driverName;

    private String driverIdCard;

    private String driverPhone;

    private String driverGender;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date driverBirthday;

    private String driverNationality;

    private String driverNation;

    private String photoId;

    private String licenseId;

    private String licensePhotoId;

    private String driverType;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date getDriverLicenseDate;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date driverLicenseOff;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date driverLicenseOn;

    private Integer commercialType;

    private Integer flag;

    private Date updateTime;

    private Date createTime;

    private String creator;

    private String modifier;

    private String reason;

    private Integer status;

    private String domain;

    private String fileNames;

    private static final long serialVersionUID = 1L;

}