package com.zhcx.netcar.pojo.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class NetcarPermitVehicleInfo implements Serializable {
    private Long uuid;

    private Long corpId;

    private String companyName;

    private String handler;

    private String handlerPhone;

    private Integer address;

    private String vehicleNo;

    private String plateColor;

    private Integer seats;

    private String brand;

    private String model;

    private String vehicleType;

    private Integer ownerType;

    private String ownerName;

    private String vehicleColor;

    private String engineId;

    private String vin;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date certifyDateA;

    private String fuelType;

    private String engineDisplace;

    private String photoId;

    private String certificate;

    private String transAgency;

    private String transArea;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date transDateStart;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date transDateStop;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date certifyDateB;

    private String fixState;

    private Date nextFixDate;

    private String checkState;

    private String feePrintId;

    private String gpsBrand;

    private String gpsModel;

    private String gpsImei;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date gpsInstallDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date registerDate;

    private Integer commercialType;

    private String fareType;

    private Integer flag;

    private String reason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String creator;

    private String modifier;

    private Integer status;

    private String domain;

    private String fileNames;

    private static final long serialVersionUID = 1L;

}