package com.zhcx.auth.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.zhcx.auth.facade.ApplicationUserService;
import com.zhcx.auth.mapper.ApplicationUserBaseInfoMapper;
import com.zhcx.auth.utils.PageHelperUtil;
import com.zhcx.auth.vo.ApplicationUserBaseInfoVO;
import io.swagger.models.auth.In;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.facade.AccountInfoService;
import com.zhcx.auth.facade.AuthUserRoleService;
import com.zhcx.auth.facade.AuthUserService;
import com.zhcx.auth.mapper.AuthUserMapper;
import com.zhcx.auth.pojo.AccountInfo;
import com.zhcx.auth.pojo.AuthUser;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.auth.pojo.AuthUserRole;
import com.zhcx.auth.utils.UUIDUtils;


@Service("authUserService")
public class AuthUserServiceImpl implements AuthUserService {

	@Resource
	private AuthUserMapper authUserMapper;
	@Resource
	private AccountInfoService accountInfoService;
	@Resource
	private AuthUserRoleService authUserRoleService;
	@Resource
	private ApplicationUserBaseInfoMapper applicationUserBaseInfoMapper;
	@Resource
	private UUIDUtils uuidUtils;

	public static final SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteAuthUser(Long uuid)throws Exception {
		Integer temp = authUserMapper.deleteByPrimaryKey(uuid);
		if(temp>0){
			AuthUserRole authUserRole = new AuthUserRole();
			authUserRole.setUserId(uuid);
			temp = authUserRoleService.deleteUserRole(authUserRole);
			if(temp>=0){
				temp = accountInfoService.deleteAccountByUserId(uuid);
				ApplicationUserBaseInfoVO applicationUserBaseInfoVO = new ApplicationUserBaseInfoVO();
				applicationUserBaseInfoVO.setUserId(uuid);
				applicationUserBaseInfoMapper.delete(applicationUserBaseInfoVO);
			}else{
				throw new Exception("删除用户信息失败");
			}
		}else{
			throw new Exception("删除用户信息失败");
		}
		return temp;
	}

	/**
	 * 新增用户、账号
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long insertAuthUser(AuthUser record)throws Exception {
		Date time = new Date();
		Long userId = uuidUtils.getLongUUID("seq:user");
		String timeStr = sFormat.format(time);
		record.setTimeCreated(timeStr);//创建时间
		record.setUuid(userId);
        int resultCode = authUserMapper.insert(record);
        if(resultCode>0){
        	AccountInfo accoutnInfo = new AccountInfo();
        	accoutnInfo.setAccountName(record.getMobilePhone());
        	accoutnInfo.setAccountPwd(record.getPassword());
        	accoutnInfo.setUserId(userId);
        	resultCode = accountInfoService.insertUserAccountInfo(accoutnInfo);
        	if(resultCode<0){
        		throw new Exception("新增账号信息失败");
        	}
        }else{
        	throw new Exception("新增用户信息失败");
        }
		return userId;
	}

	@Override
	public AuthUserResp queryDataByAccount(String account) {
		AuthUserResp user = authUserMapper.selectDataByAccount(account);
		return user;
	}

	@Override
	public AuthUser queryAuthUserByUuid(Long arg0) {
		return authUserMapper.selectAuthUserById(arg0);
	}

	/*@Override
	public List<AuthUser> queryAuthUserListByPhone(AuthUser arg0) {
		return null;
	}*/

	@Override
	public int updateAuthUser(AuthUser arg0) {
		arg0.setTimeModified(sFormat.format(new Date()));
		return authUserMapper.updateByPrimaryKey(arg0);
	}

	@Override
	public int verificationMobilePhone(String mobilePhone) {
		return authUserMapper.verificationMobilePhone(mobilePhone);
	}

	@Override
	public AuthUserResp verificationUserName(String userName) {
		return authUserMapper.verificationUserName(userName);
	}

	@Override
	public PageInfo<AuthUserResp> queryAuthUser(AuthUser record) {
		PageHelper.startPage(Integer.parseInt(record.getPageNo()), Integer.parseInt(record.getPageSize()));
		PageHelperUtil.orderBy("time_created_desc");
		List<AuthUserResp>  users = authUserMapper.selectAuthUser(record);
		AuthUserRole userRole = new AuthUserRole();
		for (AuthUserResp authUser : users) {
			String roleNames = "";
			userRole.setUserId(authUser.getUserId());
			List<AuthUserRole> roles = authUserRoleService.queryUserRoleList(userRole);
				for (int i = 0; i < roles.size(); i++) {
					roleNames = roleNames +roles.get(i).getRoleName();
					if(i<roles.size()-1){
						roleNames=roleNames+",";
					}
				}
				authUser.setRoleName(roleNames);
		}
		PageInfo<AuthUserResp> userInfoPageInfo = new PageInfo<>(users);
		return userInfoPageInfo;
	}

	@Override
	public List<AuthUserResp> queryAuthUserList(AuthUser record) {
		return authUserMapper.selectAuthUser(record);
	}
}
