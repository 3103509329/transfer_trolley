package com.zhcx.netcar.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhcx.netcar.pojo.base.BasePojo;
import lombok.Data;

import java.util.Date;

/**
 * @description: 车辆基础信息
 * @author: qzq
 * @date 2020-04-16 14:59
 **/
@Data
public class BaseInfoVehiclelicence extends BasePojo {

    private static final long serialVersionUID = 8897735242250370508L;

    private Long uuid;

    private Long corpId;

    private Long creator;

    private Long whoModified;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date timeCreated;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date timeModified;

    /**
     * 车辆id
     */
    private Long carId;

    /**
     * 机动车登记证书编号
     */
    private String docCode;

    /**
     * 车辆识别代号/车架号
     */
    private String vin;

    /**
     * 发动机号
     */
    private String ein;

    /**
     * 车牌号码
     */
    private String carNum;

    /**
     * 车辆品牌
     */
    private String carBrand;

    /**
     * 车辆厂牌型号
     */
    private String carModel;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 车辆类型
     */
    private Long cartypeId;

    /**
     * 发动机型号
     */
    private String engineType;

    /**
     * 外廓尺寸-长（毫米）
     */
    private String carSizeLong;

    /**
     * 外廓尺寸-宽（毫米）
     */
    private String carSizeWide;

    /**
     * 外廓尺寸-高（毫米）
     */
    private String carSizeHigh;

    /**
     * 车身颜色
     */
    private String carColor;

    /**
     * 车辆图片
     */
    private String carImg;

    /**
     * 车牌颜色
     */
    private String plateNumberColour;

    /**
     * 车辆燃料类型
     */
    private String fuelType;

    /**
     * 排量（毫升
     */
    private String dischargeCapacity;

    /**
     * 功率(千瓦)
     */
    private String powerQw;

    /**
     * 排放标准
     */
    private Long emissionStandard;

    /**
     * 排放标准代码 jtt 5.1
     */
    private String emissionStandardCode;

    /**
     * 车辆出厂日期
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date manufactureDate;

    /**
     * 是否空调车(1:是 2:否)
     */
    private Integer whetherAirCar;

    /**
     * 变速器型式
     */
    private Long transmissionType;

    /**
     * 总质量（千克）
     */
    private String allWeight;

    /**
     * 额定载客
     */
    private String checkPerson;

    /**
     * 座位数
     */
    private String seating;

    /**
     * 道路运输经营许可证编号
     */
    private String roadTransportCode;

    /**
     * 道路运输经营许可证文号
     */
    private String roadTransportReference;

    /**
     * 初次配发日期
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date firstAllotmentDate;

    /**
     * 发证机关
     */
    private String issuingCompany;

    /**
     * 机动车首次登记日期
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date firstAddDate;

    /**
     * 机动车所有人
     */
    private String possName;

    /**
     * 所有人证件号
     */
    private String ownerNumber;

    /**
     * 所有人证件类型
     */
    private String doucumentType;

    /**
     * 所属公司
     */
    private String subordinateCompanies;

    /**
     * 机动车登记证（主页）
     */
    private String registcertImg1;

    /**
     * 机动车登记证（附页）
     */
    private String registcertImg2;

    /**
     * 道路运输证
     */
    private String roadtransportImg;

    /**
     * 机动车行驶证
     */
    private String vehiclecertImg;

    /**
     * 车辆入户日期
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date carEnterDate;

    /**
     * 新能源车辆类别
     */
    private Long newenergyType;

    /**
     * 是否安装gps(1 是 2 否)
     */
    private Integer whetherGps;

    /**
     * 购置发票
     */
    private String purchaseBill;

    /**
     * 强制报废日期
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date retirementDate;

    /**
     * 车辆类型（字典表）
     */
    private Long opertypeId;

    /**
     * 使用性质
     */
    private Long useproperty;

    /**
     * 车籍地
     */
    private String carNativePlace;

    /**
     * 车辆技术等级：壹级、贰级
     */
    private Long gradeId;

    /**
     * 道路运输证有效期起
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date rodetransportStart;

    /**
     * 道路运输证有效期止
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date rodetransportEnd;

    /**
     * 注册日期
     */
    private String registDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 来源
     */
    private Short source;

    /**
     * 网约车企业标识
     */
    private String companyId;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 服务类型
     */
    private String commercialType;

    /**
     * 运价类型
     */
    private String fareType;

    /**
     * 车辆类型
     */
    private String vehicleType;
}
