package com.zhcx.auth.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户与运用关系表
 * t_auth_application_user
 */
@Data
public class ApplicationUserBaseInfoVO implements Serializable {
    /**
     * 
     */
    private Long uuid;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 应用编码
     */
    private String applicationCode;

    /**
     * 0.删除，1.正常，2.停用
     */
    private Integer status;

    /**
     * 
     */
    private Long creator;

    /**
     * 
     */
    private Date creatorTime;

    /**
     * 
     */
    private Date modifierTime;

    /**
     * 
     */
    private Long modifier;

    private List<String> appluicationCode;
    /**
     * t_auth_application_user
     */
    private static final long serialVersionUID = 1L;

    public ApplicationUserBaseInfoVO() {
        super();
    }
}