package com.zhcx.auth.vo;

import com.zhcx.auth.pojo.ApplicationRoleBaseInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 应用与角色关系
 * t_auth_application_role
 */
@Data
public class ApplicationRoleBaseInfoVO implements Serializable {
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

    private Integer pageNo;

    private Integer pageSize;

    private List<String> codeList;

    private Long userId;

    private String roleName;

    private List<ApplicationRoleBaseInfo> roleList;

    private String applicationName;

    private String roleType;
    /**
     * t_auth_application_role
     */
    private static final long serialVersionUID = 1L;


    public ApplicationRoleBaseInfoVO() {
        super();
    }
}