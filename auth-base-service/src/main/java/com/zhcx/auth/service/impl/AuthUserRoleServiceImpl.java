package com.zhcx.auth.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhcx.auth.facade.AuthUserRoleService;
import com.zhcx.auth.mapper.AuthUserRoleMapper;
import com.zhcx.auth.pojo.AuthUserRole;
import com.zhcx.auth.utils.UUIDUtils;

@Service("authUserRoleService")
public class AuthUserRoleServiceImpl implements AuthUserRoleService {
	
	@Autowired
	private AuthUserRoleMapper authUserRoleMapper;
	
	@Resource
	private UUIDUtils uuidUtils;
	
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public List<AuthUserRole> queryUserRoleList(AuthUserRole authUserRole) {
		List<AuthUserRole> authUserRoles = authUserRoleMapper.selectUserRoleList(authUserRole);
		return authUserRoles;
	}

	@Override
	public AuthUserRole saveUserRole(AuthUserRole authUserRole, Long userId) {
		// 生成主键id
		Long uuid = uuidUtils.getLongUUID();
		authUserRole.setUuid(uuid);
		authUserRole.setCreator(userId);
		authUserRole.setTimeCreated(new Date());
		int result = authUserRoleMapper.insert(authUserRole);
		if(result > 0) {
			return authUserRole;
		}
		return null;
	}

	@Override
	public boolean saveUserRoleBatch(List<AuthUserRole> authUserRoles, Long userId) {
		for (AuthUserRole authUserRole : authUserRoles) {
			Long uuid = uuidUtils.getLongUUID();
			authUserRole.setUuid(uuid);
			authUserRole.setCreator(userId);
			authUserRole.setTimeCreated(new Date());
		}
		// 先删除
		AuthUserRole deleteAuthUserRole = new AuthUserRole();
		AuthUserRole aur = authUserRoles.get(0);
		deleteAuthUserRole.setUserId(aur.getUserId());
		authUserRoleMapper.deleteUserRole(deleteAuthUserRole);
		// 再保存
		authUserRoleMapper.insertBatch(authUserRoles);
		return true;
	}

	@Override
	public int deleteUserRole(AuthUserRole authUserRole) {
		return authUserRoleMapper.deleteUserRole(authUserRole);
	}


}
