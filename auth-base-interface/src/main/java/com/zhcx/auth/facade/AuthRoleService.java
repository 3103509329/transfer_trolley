package com.zhcx.auth.facade;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthRole;

import java.util.List;

public interface AuthRoleService {

	int saveAuthRole(AuthRole record);
	
	int deleteRole(Long uuid);

    AuthRole queryAuthRoleByUuid(Long uuid);
    
    List<AuthRole> queryAuthRoleByParam(AuthRole recode);

    PageInfo<AuthRole> queryAuthRoleListByParam(AuthRole recode);
    
    int modifyAuthRole(AuthRole record);

    List<AuthRole> selectByUserIdAndCode(Long userId, String applicationCode);
}
