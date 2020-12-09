package com.zhcx.auth.service.impl;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.zhcx.auth.facade.ApplicationService;
import com.zhcx.auth.mapper.ApplicationBaseInfoMapper;
import com.zhcx.auth.pojo.ApplicationBaseInfo;
import org.apache.catalina.core.ApplicationMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.facade.AuthRoleService;
import com.zhcx.auth.mapper.AuthRoleMapper;
import com.zhcx.auth.pojo.AuthRole;
import com.zhcx.auth.utils.UUIDUtils;

@Service("authRoleService")
public class AuthRoleServiceImpl implements AuthRoleService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthRoleMapper authRoleMapper;

    @Resource
    private UUIDUtils uuidUtils;

    @Autowired
    private ApplicationBaseInfoMapper applicationBaseInfoMapper;

    @Override
    public int saveAuthRole(AuthRole record) {
        record.setUuid(uuidUtils.getLongUUID("seq:car-hailing:auth:role"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ctime = sdf.format(new Date());
        record.setTimeCreated(ctime);
        return authRoleMapper.insertSelective(record);
    }

    @Override
    public AuthRole queryAuthRoleByUuid(Long uuid) {
        return authRoleMapper.selectByPrimaryKey(uuid);
    }

/*	@Override
	public AuthRole selectAuthRoleByParam(AuthRole recode) {
		// TODO Auto-generated method stub
		return null;
	}*/

    @Override
    public PageInfo<AuthRole> queryAuthRoleListByParam(AuthRole recode) {
        PageHelper.startPage(Integer.parseInt(recode.getPageNo()), Integer.parseInt(recode.getPageSize()));
        List<AuthRole> authRoles = authRoleMapper.selectAuthRoleListByParam(recode);

        List<ApplicationBaseInfo> applicationBaseInfos = applicationBaseInfoMapper.select(null);
        Map<String, List<ApplicationBaseInfo>> applicationMap = applicationBaseInfos.stream().collect(Collectors.groupingBy(ApplicationBaseInfo::getCode));
        authRoles.forEach(a -> {
            if (a.getApplicationCode() != null) {
                List<ApplicationBaseInfo> applicationBaseInfoList = applicationMap.get(a.getApplicationCode());
                a.setApplicationName(applicationBaseInfoList != null ? applicationBaseInfoList.get(0).getName() : null);
            }
        });
        PageInfo<AuthRole> authRolesPageInfo = new PageInfo<>(authRoles);
        return authRolesPageInfo;
    }

    @Override
    public int modifyAuthRole(AuthRole record) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ctime = sdf.format(new Date());
        record.setTimeModified(ctime);
        return authRoleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int deleteRole(Long uuid) {
        return authRoleMapper.deleteByPrimaryKey(uuid);
    }

    @Override
    public List<AuthRole> queryAuthRoleByParam(AuthRole recode) {
        return authRoleMapper.selectAuthRoleByParam(recode);
    }

    @Override
    public List<AuthRole> selectByUserIdAndCode(Long userId, String applicationCode) {
        return authRoleMapper.selectByUserIdAndCode(userId, applicationCode);
    }
}
