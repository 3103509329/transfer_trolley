package com.zhcx.auth.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhcx.auth.dubbo.AuthUserRoleDubboService;
import com.zhcx.auth.mapper.AuthUserRoleMapper;
import com.zhcx.auth.pojo.AuthUserRole;
import com.zhcx.auth.utils.UUIDUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Service(version = "${zhcx.business.dubbo.version}",interfaceClass = AuthUserRoleDubboService.class)
public class AuthUserRoleDubboServiceImpl implements AuthUserRoleDubboService {
	
	@Resource
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
