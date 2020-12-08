package com.zhcx.auth.dubbo;

import com.zhcx.auth.pojo.AuthUserRole;

import java.util.List;

/**
 * 用户角色关系业务接口
 * 
 * @title
 * @author tangding
 * @date 2018年11月23日
 * @version 1.0
 */
public interface AuthUserRoleDubboService {

	/**
	 * 根据条件查询用户角色关系数据集合
	 * @param authUserRole
	 * @return
	 */
	List<AuthUserRole> queryUserRoleList(AuthUserRole authUserRole);

	/**
	 * 单个保存用户角色权限关系
	 * @param authUserRole
	 * @return
	 */
	AuthUserRole saveUserRole(AuthUserRole authUserRole, Long userId);

	/**
	 * 批量保存用户角色权限关系
	 * @param authUserRoles
	 * @return
	 */
	boolean saveUserRoleBatch(List<AuthUserRole> authUserRoles, Long userId);

	/**
	 * 删除用户角色权限关系
	 * @param authUserRole
	 * @return
	 */
	int deleteUserRole(AuthUserRole authUserRole);
	
}
