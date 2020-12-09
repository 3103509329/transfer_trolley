package com.zhcx.auth.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用与角色关系
 * t_auth_application_role
 */
@Data
public class ApplicationRoleBaseInfo implements Serializable {
    /**
     * 
     */
    private Long uuid;

    /**
     * 角色
     */
    private Long roleId;

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
     * t_auth_application_role
     */
    private static final long serialVersionUID = 1L;

    public ApplicationRoleBaseInfo(Long uuid, Long roleId, String applicationCode, Integer status, Long creator, Date creatorTime, Date modifierTime, Long modifier) {
        this.uuid = uuid;
        this.roleId = roleId;
        this.applicationCode = applicationCode;
        this.status = status;
        this.creator = creator;
        this.creatorTime = creatorTime;
        this.modifierTime = modifierTime;
        this.modifier = modifier;
    }

    public ApplicationRoleBaseInfo() {
        super();
    }
}