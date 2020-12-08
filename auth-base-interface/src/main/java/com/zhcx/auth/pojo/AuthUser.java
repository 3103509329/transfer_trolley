package com.zhcx.auth.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthUser implements Serializable {
    private Long uuid;
    
    /*private Long uuids;*/

    private String userName;
    
    private String nikeName;

    private Long corpId;

    private String userType;

    private String email;

    private String mobilePhone;

    private Integer userStatus;

    private String dateFrom;

    private String dateTo;

    private String activedate;

    private Long creator;

    private String timeCreated;

    private Long whoModified;

    private String timeModified;

    private String source;

    private String userImg;

    private String userExt1;
    /**
     * 账号密码
     */
    private String password;
    /**
     * 当前页码
     */
    private String pageNo;
    
    /**
     * 每一页的记录条数
     */
    private String pageSize;
    
    private String orderBy = null;

  	private String sortType = "ASC";

  	private String roleName;

  	private String remark;
    private static final long serialVersionUID = 1L;

    private String applicationCode;

    private Long userId;
}