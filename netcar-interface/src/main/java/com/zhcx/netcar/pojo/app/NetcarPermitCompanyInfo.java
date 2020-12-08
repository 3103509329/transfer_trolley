package com.zhcx.netcar.pojo.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class NetcarPermitCompanyInfo implements Serializable {
    private Long uuid;

    private String companyId;

    private String companyName;

    private String handler;

    private String handlerPhone;

    private String identifier;

    private Integer address;

    private String businessScope;

    private String contactAddress;

    private String economicType;

    private String regCapital;

    private String legalName;

    private String legalId;

    private String legalPhone;

    private String legalPhoto;

    private String reason;

    private Integer flag;

    //企业经营状态
    private Integer state;

    //企业经营区域
    private String manageScope;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String creator;

    private Integer status;

    private String domain;

    private String fileNames;

    private static final long serialVersionUID = 1L;

}