package com.zhcx.netcar.pojo.mapAndVideo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 网约车第三方平台数据表
 * netcar_third_party
 */
@Data
public class NetcarThirdParty implements Serializable {
    /**
     * 
     */
    private Long uuid;

    /**
     * 网约车企业标识
     */
    private String companyId;

    /**
     * 统一社会信用代码
     */
    private String identifier;

    /**
     * 业户名称(企业名称)
     */
    private String companyName;

    /**
     * 账号名称
     */
    private String accountName;

    /**
     * 密码
     */
    private String accountPwd;

    /**
     * 密钥类型(0：MD5，1：AES，2：明文，3：RSA)
     */
    private String keyType;

    /**
     * 创建人
     */
    private String create;

    /**
     * 创建人
     */
    private String update;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 地址
     */
    private String path;

    /**
     * netcar_third_party
     */
    private static final long serialVersionUID = 1L;
}