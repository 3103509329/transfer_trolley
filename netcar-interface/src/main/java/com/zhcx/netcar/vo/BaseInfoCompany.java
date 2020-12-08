package com.zhcx.netcar.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhcx.netcar.pojo.base.BasePojo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 企业基础信息实体类
 * @author: qzq
 * @date 2020-04-15 11:39
 **/
@Data
public class BaseInfoCompany extends BasePojo implements Serializable {

    private static final long serialVersionUID = 6123206703311702069L;

    private Long corpId;

    /**
     * 业户名称(企业名称)
     */
    private String companyName;

    /**
     * 企业简称
     */
    private String enterpriseName;

    /**
     * 公司性质
     */
    private String companyNature;

    /**
     * 省
     */
    private Long province;

    /**
     * 市
     */
    private Long city;

    /**
     * 区
     */
    private Long district;

    /**
     * 公司法人
     */
    private String egalRepName;

    /**
     * 法人代表电话
     */
    private String legalPhone;

    /**
     * 统一社会信用代码
     */
    private String identifier;

    /**
     * 法人代表身份证号
     */
    private String legalId;

    /**
     * 经营范围
     */
    private String businessScope;

    /**
     * 法人代表身份证扫描件文件编号
     */
    private String legalPhoto;

    /**
     * 通信地址
     */
    private String contactAddress;

    /**
     * 经营业户经济范围
     */
    private String economicType;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 注册号
     */
    private String regisNumber;

    /**
     * 注册资本
     */
    private String regisCapital;

    /**
     * 成立日期
     */
    private String establishmentDate;

    /**
     * 营业开始时间
     */
    private String businessStartTime;

    /**
     * 营业结束时间
     */
    private String businessEndTime;

    /**
     * 附件
     */
    private String file;

    /**
     * 企业类型（公交，出租，农客，网约车）
     */
    private Integer corpType;

    /**
     * 操作标识 1新增 2更新 3删除
     */
    private Integer flag;

    /**
     * 经营业户经济类型gb 12402
     */
    private String economicCategory;

    /**
     * 经营业户经济类型代码
     */
    private String economicCategoryCode;

    /**
     * 业主状况代码
     */
    private String ownerStatusCode;

    /**
     * 经营许可证号jt/t 415
     */
    private String operationPermitNumber;

    /**
     * 经营许可证字
     */
    private String operationPermitWord;

    /**
     * 经营业户行政区划代码
     */
    private String ownerDivisionCode;

    /**
     * 经营业户行政区划
     */
    private String ownerDivision;

    /**
     * 状态(本地维护0-无效,1有效)
     */
    private Integer status;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 修改人
     */
    private Long whoModified;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date timeCreated;

    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date timeModified;

    /**
     * 组织机构代码 gb 11714
     */
    private String organizationName;

    /**
     * 来源
     */
    private Short source;

    /**
     * 网约车企业标识
     */
    private String companyId;

    /**
     * 经营许可证有效期起
     */
    private String operationPermitFrom;

    /**
     * 经营许可证有效期止
     */
    private String operationPermitTo;

    /**
     * 经营许可证发证机关
     */
    private String operationLicenseIssuing;

    /**
     * 经营许可证初次发证日期
     */
    private String operationLicenseFrom;

    /**
     * 负责人姓名
     */
    private String responsiblePerson;

}
