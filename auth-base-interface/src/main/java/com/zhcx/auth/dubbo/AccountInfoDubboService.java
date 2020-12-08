package com.zhcx.auth.dubbo;

import com.zhcx.auth.pojo.AccountInfo;

import java.util.List;

public interface AccountInfoDubboService {

	List<AccountInfo> selectByAccountName(AccountInfo record);
	
	int insertUserAccountInfo(AccountInfo accountInfo);

	//int updateUserInsertAccout(AccountInfo accountInfo,AuthUser authUser);

	/**
    * *忘记密码、修改密码
    * @param record
    * @return
    */
    AccountInfo updateUserAccoutnInfo(AccountInfo record);

    /**
     * *验证账号密码
     * @param record
     * @return
     */
	int verifyAccount(String account, String pwd);

	 /**
     * *移动端验证账号，密码
     * @param record
     * @return
     */
	int verifyMobileAccount(String account, String password);

	 /**
     * *验证账号是否已存在
     * @param record
     * @return
     */
	int verificationAccount(String account);
	
	int deleteAccountByUserId(Long userId);

}
