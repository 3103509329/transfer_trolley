package com.zhcx.auth.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class AuthRole implements Serializable {

    private static final long serialVersionUID = 11L;

    private Long uuid;

    private String roleName;

    private String roleCode;

    private String creator;

    private String whoModified;

    private String timeCreated;

    private String timeModified;

    private String status;

    private String grade;

    private String str1;

    private String sysTypes;

    private String pageNo;

    private String pageSize;

    private String applicationCode;

    private String roleType;

    private String applicationName;


    public AuthRole() {
    }


}