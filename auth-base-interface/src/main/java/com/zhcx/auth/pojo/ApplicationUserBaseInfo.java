package com.zhcx.auth.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户与运用关系表
 * t_auth_application_user
 */
@Data
public class ApplicationUserBaseInfo implements Serializable {
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

    /**
     * t_auth_application_user
     */
    private static final long serialVersionUID = 1L;

    public ApplicationUserBaseInfo(Long uuid, Long userId, String applicationCode, Integer status, Long creator, Date creatorTime, Date modifierTime, Long modifier) {
        this.uuid = uuid;
        this.userId = userId;
        this.applicationCode = applicationCode;
        this.status = status;
        this.creator = creator;
        this.creatorTime = creatorTime;
        this.modifierTime = modifierTime;
        this.modifier = modifier;
    }

    public ApplicationUserBaseInfo() {
        super();
    }
}