package com.zhcx.auth.pojo;

import lombok.Data;

@Data
public class AuthUserResp  implements java.io.Serializable{
	
	private static final long serialVersionUID = -6178373833080704515L;

	private String accountName;
	
	private String accountType;
	
	private String accountStatus;
	
	private Long userId;
	
	private String phone;
	
	//昵称
	private String nikeName;
    //用户名
    private String userName;
    //密码
    private String password;
    //颜值
    private String saltDensityValue;
    //企业ID
    private Long corpId;
    //用户类型
    private String userType;
    //邮箱
    private String email;
  //电话号码
    private String mobilePhone;
    //用户状态
    private Integer userStatus;
    //生效时间
    private String dateFrom;
    //失效时间
    private String dateTo;
    //激活时间
    private String activedate;
    //停用时间
    private String enddate;
    //创建人
    private String creator;
    //创建时间
    private String timeCreated;
    //修改人
    private String whoModified;
    //修改时间
    private String timeModified;
    //注册通道
    private String channel;
    //来源
    private String source;

    private String userImg;
    
    private String userExt1;

    private String userExt2;

    private String userExt3;

    private String userExt4;
    
    private String roleName;
    
    //企业名称
    private String corpName;

    private String remark;

}
