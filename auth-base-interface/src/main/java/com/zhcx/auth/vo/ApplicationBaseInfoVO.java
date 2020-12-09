package com.zhcx.auth.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 平台应用
 * t_auth_application
 */
@Data
public class ApplicationBaseInfoVO implements Serializable {
    /**
     * 
     */
    private Long uuid;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

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
    private String creatorTime;

    /**
     * 
     */
    private String modifierTime;

    /**
     * 
     */
    private Long modifier;

    private String remark;
    /**
     * 当前页码
     */
    private Integer pageNo;

    /**
     * 每一页的记录条数
     */
    private Integer pageSize;

    private List<ApplicationRoleBaseInfoVO> roleList;

    private Long userId;

    private List<String> codeList;

    private Long roleId;
    /**
     * t_auth_application
     */
    private static final long serialVersionUID = 1L;
}