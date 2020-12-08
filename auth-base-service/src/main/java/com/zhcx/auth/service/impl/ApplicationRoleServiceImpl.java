package com.zhcx.auth.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.facade.ApplicationRoleService;
import com.zhcx.auth.mapper.ApplicationBaseInfoMapper;
import com.zhcx.auth.mapper.ApplicationRoleBaseInfoMapper;
import com.zhcx.auth.mapper.ApplicationUserBaseInfoMapper;
import com.zhcx.auth.mapper.AuthUserRoleMapper;
import com.zhcx.auth.pojo.ApplicationRoleBaseInfo;
import com.zhcx.auth.pojo.ApplicationUserBaseInfo;
import com.zhcx.auth.pojo.AuthUserRole;
import com.zhcx.auth.utils.UUIDUtils;
import com.zhcx.auth.vo.ApplicationBaseInfoVO;
import com.zhcx.auth.vo.ApplicationRoleBaseInfoVO;
import com.zhcx.auth.vo.ApplicationUserBaseInfoVO;

/**
 * @ClassName：ApplicationRoleServiceImpl
 * @Description:
 * @author：李亮
 * @date：2020/4/216:47
 */
@Service("applicationRoleService")
public class ApplicationRoleServiceImpl implements ApplicationRoleService {

    static final String APPLICATIONROLE = "ApplicationRole";
    static final String APPLICATIONUSERROLE = "ApplicationUserRole";
    @Resource
    private UUIDUtils uuidUtils;
    @Autowired
    private ApplicationRoleBaseInfoMapper applicationRoleBaseInfoMapper;
    @Autowired
    private ApplicationUserBaseInfoMapper applicationUserBaseInfoMapper;
    @Autowired
    private ApplicationBaseInfoMapper applicationMapping;
    @Autowired
    private AuthUserRoleMapper authUserRoleMapper;

    @Override
    @Transactional
    public Boolean add(ApplicationRoleBaseInfoVO applicationRoleBaseInfoVO) {

        if (null == applicationRoleBaseInfoVO || null == applicationRoleBaseInfoVO.getUserId()) {
            return false;
        }
        //应用解绑
        ApplicationUserBaseInfoVO userDel = new ApplicationUserBaseInfoVO();
        userDel.setUserId(applicationRoleBaseInfoVO.getUserId());
        int userDelResult = applicationUserBaseInfoMapper.delete(userDel);
        //用户角色解绑
        AuthUserRole authUserRole = new AuthUserRole();
        authUserRole.setUserId(applicationRoleBaseInfoVO.getUserId());
        int authUserDel = authUserRoleMapper.deleteUserRole(authUserRole);

        //用户/应用/角色关系绑定
        List<AuthUserRole> rolelist = new ArrayList<>();
        List<ApplicationUserBaseInfo> userList = new ArrayList<>();
        if (null != applicationRoleBaseInfoVO.getRoleList() && applicationRoleBaseInfoVO.getRoleList().size() > 0) {
            applicationRoleBaseInfoVO.getRoleList().forEach(a -> {

                //删除绑定应用与角色的绑定
                if (null == a.getApplicationCode()) {
                    return;
                }
                //用户与应用绑定
                ApplicationUserBaseInfo applicationUserBaseInfo = new ApplicationUserBaseInfo();
                applicationUserBaseInfo.setUuid(uuidUtils.getLongUUID(APPLICATIONROLE));
                applicationUserBaseInfo.setUserId(applicationRoleBaseInfoVO.getUserId());
                applicationUserBaseInfo.setApplicationCode(a.getApplicationCode());
                applicationUserBaseInfo.setCreatorTime(new Date());
                applicationUserBaseInfo.setModifierTime(new Date());
                userList.add(applicationUserBaseInfo);
                //应用与角色绑定
                if (null != a.getRoleId()) {
                    AuthUserRole userRole = new AuthUserRole();
                    userRole.setUuid(uuidUtils.getLongUUID(APPLICATIONUSERROLE));
                    userRole.setUserId(applicationRoleBaseInfoVO.getUserId());
                    userRole.setRoleId(a.getRoleId());
                    userRole.setTimeCreated(new Date());
                    userRole.setTimeModified(new Date());
                    rolelist.add(userRole);
                }
            });
        } else {
            return true;
        }
        if (rolelist.size() > 0) {
            int roleResult = authUserRoleMapper.insertList(rolelist);
            if (roleResult <= 0) {
                return false;
            }
        }
        if (userList.size() > 0) {
            int userResult = applicationUserBaseInfoMapper.insertList(userList);
            if (userResult <= 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Integer update(List<ApplicationBaseInfoVO> applicationBaseInfoList) {

        if (null == applicationBaseInfoList || applicationBaseInfoList.size() <= 0) {
            return null;
        }
        List<ApplicationRoleBaseInfo> list = new ArrayList<>();
        applicationBaseInfoList.forEach(a -> {
            ApplicationRoleBaseInfoVO applicationRoleBaseInfoVO = new ApplicationRoleBaseInfoVO();
            if (null == a.getRoleId()) {
                applicationRoleBaseInfoVO.setApplicationCode(a.getCode());
                applicationRoleBaseInfoVO.setRoleId(a.getRoleId());
                applicationRoleBaseInfoMapper.delete(applicationRoleBaseInfoVO);
            }
            applicationRoleBaseInfoVO.setUuid(uuidUtils.getLongUUID(APPLICATIONROLE));
            applicationRoleBaseInfoVO.setCreatorTime(new Date());
            applicationRoleBaseInfoVO.setCreator(a.getCreator());
            applicationRoleBaseInfoVO.setModifierTime(new Date());
            ApplicationRoleBaseInfo applicationRoleBaseInfo = new ApplicationRoleBaseInfo();
            BeanUtils.copyProperties(applicationRoleBaseInfoVO, applicationRoleBaseInfo);
            list.add(applicationRoleBaseInfo);
        });
        int resultData = applicationRoleBaseInfoMapper.insertList(list);
        return resultData;
    }

    @Override
    public PageInfo<ApplicationRoleBaseInfo> select(ApplicationRoleBaseInfoVO applicationRoleBaseInfoVO) {
        List<ApplicationUserBaseInfo> applicationUserBaseInfoList = new ArrayList<>();
        if (null != applicationRoleBaseInfoVO.getUserId()) {
            ApplicationUserBaseInfo applicationUserBaseInfo = new ApplicationUserBaseInfo();
            applicationUserBaseInfo.setUserId(applicationRoleBaseInfoVO.getUserId());
//            applicationUserBaseInfoList = applicationUserBaseInfoMapper.selectByUserId(applicationUserBaseInfo);
        }
        if (null != applicationUserBaseInfoList && applicationUserBaseInfoList.size() > 0) {

        }
        return new PageInfo<>(applicationRoleBaseInfoMapper.select(applicationRoleBaseInfoVO));
    }

    @Override
    public Integer delete(ApplicationRoleBaseInfoVO applicationRoleBaseInfoVO) {
        return applicationRoleBaseInfoMapper.delete(applicationRoleBaseInfoVO);
    }

//    @Override
//    public PageInfo<ApplicationBaseInfoVO> selectByRole(AuthUserResp authUser) {
//
//        ApplicationUserBaseInfo applicationUserBaseInfo = new ApplicationUserBaseInfo();
//        List<AuthUserRole> authUserRoles = new ArrayList<>();
//        ApplicationRoleBaseInfoVO role = new ApplicationRoleBaseInfoVO();
//
//        List<ApplicationBaseInfoVO> applicationBaseInfos = new ArrayList<>();
//        if (null != authUser && null != authUser.getUserId() && !authUser.getUserId().equals("")) {
//            applicationUserBaseInfo.setUserId(authUser.getUserId());
//            AuthUserRole authUserRole = new AuthUserRole();
//            authUserRole.setUserId(authUser.getUserId());
//            authUserRoles = authUserRoleMapper.selectByuser(authUserRole);
//        }
//        applicationBaseInfos = applicationMapping.selectByRole(applicationUserBaseInfo);
//        if (null != applicationBaseInfos && applicationBaseInfos.size() > 0) {
//            role.setCodeList(applicationBaseInfos.stream().map(ApplicationBaseInfoVO::getCode).collect(Collectors.toList()));
//            if (null != authUserRoles && authUserRoles.size() > 0) {
//                role.setRoleList(authUserRoles.stream().map(AuthUserRole::getRoleId).collect(Collectors.toList()));
//            }
//        }
//        List<ApplicationRoleBaseInfo> roleList = applicationRoleBaseInfoMapper.selectBycode(role);
//        if (null != roleList && roleList.size() > 0) {
//            Map<String, List<ApplicationRoleBaseInfo>> map = roleList.stream().collect(Collectors.groupingBy(ApplicationRoleBaseInfo::getApplicationCode));
////            applicationBaseInfos.forEach(a -> a.setRoleList(map.get(a.getCode())));
//        }
//        return new PageInfo<>(applicationBaseInfos);
//    }
}
