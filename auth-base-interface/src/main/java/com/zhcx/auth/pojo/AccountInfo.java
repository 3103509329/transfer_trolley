package com.zhcx.auth.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountInfo implements Serializable {
    private Long uuid;

    private String accountName;

    private String accountPwd;

    private String accountStatus;

    private String delFlag;

    private String createTime;

    private Long createUserId;

    private String accountType;

    private String keyType;

    private String secretKey;

    private String pwdCallWay;

    private String securityEmail;

    private String lastEditTime;

    private Long userId;

    private String remark;

    private String phone;

    private String salt;
    
    //-------新加参数------
    private String romdom;//验证码
    
    private Integer userStatus;
    
    private String userType;
    
    private Long corpId;
    //标识符（用来标识是新增还是修改）
    private boolean isTag;
    //用户名
    private String userName;

    private String oldPwd;

    //任务权限
    private String userExt1;

}