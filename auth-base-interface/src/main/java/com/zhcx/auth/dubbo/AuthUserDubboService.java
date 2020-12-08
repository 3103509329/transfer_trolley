package com.zhcx.auth.dubbo;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUser;
import com.zhcx.auth.pojo.AuthUserResp;

import java.util.List;

public interface AuthUserDubboService {
	
	int deleteAuthUser(Long uuid)throws Exception ;

	Long insertAuthUser(AuthUser record)throws Exception ;

    AuthUser queryAuthUserByUuid(Long uuid);

    int updateAuthUser(AuthUser record);
    
    //List<AuthUser> queryAuthUserListByPhone(AuthUser recode);
    
    /**
	 * 
	 * @Title: queryAuthUser 
	 * @Description: TODO 根据用户对象查找用户集合（仅用户列表使用）
	 * @param @param AuthUser
	 * @param @return
	 * @return AuthUser
	 * @throws
	 */
	PageInfo<AuthUserResp> queryAuthUser(AuthUser record);


	/**
	 * 
	 * @Title: queryDataByAccount 
	 * @Description: TODO 根据账号信息查询用户数据
	 * @param @param account
	 * @param @return
	 * @return AuthUserResp
	 * @throws
	 */
	public AuthUserResp queryDataByAccount(String account);
	
	/**
	 * 
	 * @Title: verificationMobilePhone 
	 * @Description: TODO 验证手机号是否存在
	 * @param @param account
	 * @param @return
	 * @return int
	 * @throws
	 */
	int verificationMobilePhone(String mobilePhone);
	/**
	 * 验证用户是否存在（loginName）
	 * @param loginName
	 * @return
	 */
	AuthUserResp verificationUserName(String loginName);
	
	/**
	 * 获取用户集合
	 * @param 用户对象
	 * @return
	 */
	List<AuthUserResp> queryAuthUserList(AuthUser record);
}
