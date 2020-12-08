package com.zhcx.auth.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zhcx.auth.facade.AccountInfoService;
import com.zhcx.auth.mapper.AccountInfoMapper;
import com.zhcx.auth.pojo.AccountInfo;
import com.zhcx.auth.utils.MD5Util;
import com.zhcx.auth.utils.UUIDUtils;

@Service("accountInfoService")
public class AccountInfoServiceImpl implements AccountInfoService {

//	@Autowired
//	PasswordEncoder passwordEncoder;

	@Autowired
	private AccountInfoMapper accountInfoMapper;
	@Resource
	private UUIDUtils uuidUtils;

	public static final SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public int verifyAccount(String account,String pwd) {

		AccountInfo info = accountInfoMapper.verifyAccount(account);

		int temp = 0;
		if(info==null){//账号不存在
			return temp = -1;
		}
		/*if("11".equals(info.getUserType())||"05".equals(info.getUserType())){//用户类型为司机或者个人用户
			return temp = 11;
		}*/

		if(0==info.getUserStatus()){//用户账号已禁用
			return temp = 0;
		}
		if("2"==info.getDelFlag()){//用户账号已锁定
			return temp = 3;
		}
		String mpwd = MD5Util.MD5Encode(MD5Util.MD5Encode(pwd)+info.getSalt());   //密码加密   [MD5（MD5加密+颜值）]

		if(mpwd.equals(info.getAccountPwd())){
			temp = 1;
		}else{
			temp = -1;
		}
		return  temp;

	}

	@Override
	public int verifyMobileAccount(String account,String pwd) {
		AccountInfo info = accountInfoMapper.verifyAccount(account);

		int temp = 0;
		if(info==null){//账号不存在
			return temp = -1;
		}
		if(0==info.getUserStatus()){//用户账号已禁用
			return temp = 0;
		}
		if("2"==info.getDelFlag()){//用户账号已锁定
			return temp = 3;
		}
		String mpwd = MD5Util.MD5Encode(MD5Util.MD5Encode(pwd)+info.getSalt());   //密码加密   [MD5（MD5加密+颜值）]

		if(mpwd.equals(info.getAccountPwd())){
			temp = 1;
		}else{
			temp = -1;
		}
		return  temp;
	}



	/**忘记密码
	 * *
	 */
	@Override
	public AccountInfo updateUserAccoutnInfo(AccountInfo accountInfo) {
		Date time = new Date();
		String saltDensityValue = time.getTime()+"";//颜值

		/*if(!StringUtil.isNullOrEmpty(empId)){
		   //插入修改密码日志表
		   PwdLog pwdLog = new PwdLog();
		   pwdLog.setNumber(IdUtils.pkId());//UUID
		   pwdLog.setPwdChangeTime(sFormat.format(new Date()));//密码变更时间
		   pwdLog.setPwdPhone(accountInfo.getPhone());//密码变更手机号
		   pwdLog.setPwdChangerid(empId);//密码变更者ID
		   pwdLog.setPwdChangerDep(depId);//密码变更者所属部门
		   pwdLog.setState(ConstantCode.PWD_STATUS_SUCCESS);//密码变更状态0：失败 1：成功
		   int pwdCode = pwdLogDao.insertPwdLog(pwdLog);
		   if(pwdCode<1){
			   return null;
		   }
	   	}*/
		accountInfo.setSalt(saltDensityValue);//颜值
		String mpwd = MD5Util.MD5Encode(MD5Util.MD5Encode(accountInfo.getAccountPwd())+saltDensityValue);
		accountInfo.setAccountPwd(mpwd);//密码（MD5加密+颜值）
		accountInfo.setLastEditTime(sFormat.format(new Date()));//上一次修改密码的时间
		int resultCode = accountInfoMapper.updateByPrimaryKey(accountInfo);
	    if (resultCode < 0){
	    	return null;
	    }
		return accountInfo;
	}

	@Override
	public int verificationAccount(String accountName) {
		AccountInfo info = new AccountInfo();
		info.setAccountName(accountName);
		List<AccountInfo> list = accountInfoMapper.selectByAccountName(info);
		if(list.size()>0){
			return 1;
		}
		return 0;
	}

	@Override
	public List<AccountInfo> selectByAccountName(AccountInfo record) {
		return this.accountInfoMapper.selectByAccountName(record);
	}

	public static void main(String[] args) {
		String saf = new Date().getTime()+"";
		String mpwd = MD5Util.MD5Encode(MD5Util.MD5Encode("123456")+(saf));
		System.out.println(saf);
		System.out.println(mpwd);
	}

	@Override
	public int insertUserAccountInfo(AccountInfo accountInfo) {

		Date time = new Date();
		String timeStr = sFormat.format(time);
		String saltDensityValue = time.getTime()+"";//颜值
		accountInfo.setUuid(uuidUtils.getLongUUID("seq:accountInfo"));//uuid
		accountInfo.setAccountStatus("0");//状态(0：正常，1：待审核，2：审核不通过，3：注销，4：异常)
		accountInfo.setCreateTime(timeStr);//创建时间
		accountInfo.setKeyType("0");//密钥类型(0：MD5，1：AES，2：明文，3：RSA)
		accountInfo.setDelFlag("0");//删除标记(0：正常，1：删除，2：锁定)
		accountInfo.setAccountType("0");//账号类型(0：平台账号，1：系统账号，2：程序账号)
		accountInfo.setPwdCallWay("0");//密码找回方式(0：EMail)
		accountInfo.setSalt(saltDensityValue);//颜值
		String mpwd = MD5Util.MD5Encode(MD5Util.MD5Encode(accountInfo.getAccountPwd())+saltDensityValue);
		accountInfo.setAccountPwd(mpwd);//密码（MD5加密+颜值）
		//accountInfo.setUserId(userId);//用户ID
		accountInfo.setLastEditTime(timeStr);//上一次修改密码的时间
		return accountInfoMapper.insert(accountInfo);
	}

	@Override
	public int deleteAccountByUserId(Long userId) {
		return accountInfoMapper.deleteAccountByUserId(userId);
	}

}
