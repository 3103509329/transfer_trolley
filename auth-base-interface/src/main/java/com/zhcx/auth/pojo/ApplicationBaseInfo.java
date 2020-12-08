package com.zhcx.auth.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 平台应用
 * t_auth_application
 */
@Data
public class ApplicationBaseInfo implements Serializable {
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
     * t_auth_application
     */
    private static final long serialVersionUID = 1L;

    public ApplicationBaseInfo(Long uuid, String name, String code, Integer status, Long creator, String creatorTime, String modifierTime, Long modifier, String remark) {
        this.uuid = uuid;
        this.name = name;
        this.code = code;
        this.status = status;
        this.creator = creator;
        this.creatorTime = creatorTime;
        this.modifierTime = modifierTime;
        this.modifier = modifier;
        this.remark = remark;
    }

    public ApplicationBaseInfo() {
        super();
    }
}