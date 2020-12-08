package com.zhcx.netcar.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhcx.netcar.pojo.base.BasePojo;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: qzq
 * @date 2020-04-16 14:07
 **/
@Data
public class BaseInfoEmpl extends BasePojo {

    private static final long serialVersionUID = -1602990553797133918L;

    private Long uuid;

    private Long userId;

    private Long corpId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 工号
     */
    private String workNo;

    /**
     * 性别\r\n
     */
    private String gender;

    /**
     * 出生日期\r\n
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    /**
     * 身份证类型\r\n
     */
    private String idcType;

    /**
     * 身份证号码\r\n
     */
    private String idcNum;

    /**
     * 员工卡号\r\n
     */
    private String cardNumber;

    /**
     * 民族代码\r\n
     */
    private String nationCode;

    /**
     * 籍贯\r\n
     */
    private String nativePlace;

    /**
     * 婚姻状态（0：未婚，1：已婚，2：离异，3：丧偶）\r\n
     */
    private Short maritalStatus;

    /**
     * 学历代码\r\n
     */
    private String degreeCode;

    /**
     * 联系电话\r\n
     */
    private String phoneNo;

    /**
     * 紧急联系人\r\n
     */
    private String emergencyContact;

    /**
     * 紧急联系人电话\r\n
     */
    private String emergencyContactPhone;

    /**
     * 紧急联系人联系地址\r\n
     */
    private String emergencyContactAddress;

    /**
     * 健康情况代码\r\n
     */
    private String healthCode;

    /**
     * 政治面貎代码\r\n
     */
    private String polAffCode;

    /**
     * 现居住地址\r\n
     */
    private String presentAddr;

    /**
     * 初次上岗日期\r\n
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date initialWorkTime;

    /**
     * 在岗情况(0-不在岗 1-在岗)
     */
    private Short statusOfOnpost;

    /**
     * 工种类型\r\n
     */
    private String proCatcode;

    /**
     * 有效开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effStartTime;

    /**
     * 有效结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effEndTime;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeCreated;

    /**
     * 修改人
     */
    private Long whoModified;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeModified;

    private String remark;

    /**
     * 1有效，0无效
     */
    private Short status;

    /**
     * 来源
     */
    private Short source;

    /**
     * 绑定状态
     */
    private String bindStatus;

    /**
     * 网约车企业标识
     */
    private String companyId;

    /**
     * 企业名称
     */
    private String companyName;

    private Integer address;

    private String driverCensus;

    private String driverAddress;

    private String driverContactAddress;

    private String photoId;

    private String licensePhotoId;

    private String driverType;

    private String getDriverLicenseDate;

    private String driverLicenseOff;

    private String driverLicenseOn;

    private Integer taxiDriver;

    private String certificateNo;

    private String networkCarIssueOrganization;

    private String networkCarIssueDate;

    private String getNetworkCarProofDate;

    private String networkCarProofOn;

    private String networkCarProofOff;

    private String registerDate;

    private Integer fullTimeDriver;

    private Integer inDriverBlacklist;

    private Integer commercialType;

    private String contractCompany;

    private String contractOn;

    private String contractOff;

    private String licenseId;

    private String emplType;
}
