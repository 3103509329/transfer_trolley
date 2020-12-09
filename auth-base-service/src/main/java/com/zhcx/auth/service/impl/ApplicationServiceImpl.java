package com.zhcx.auth.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.zhcx.auth.pojo.*;
import com.zhcx.auth.utils.PageHelperUtil;
import com.zhcx.auth.vo.ApplicationUserBaseInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.facade.ApplicationService;
import com.zhcx.auth.mapper.ApplicationBaseInfoMapper;
import com.zhcx.auth.mapper.ApplicationRoleBaseInfoMapper;
import com.zhcx.auth.mapper.ApplicationUserBaseInfoMapper;
import com.zhcx.auth.mapper.AuthMenuRelationMapper;
import com.zhcx.auth.mapper.AuthRoleMapper;
import com.zhcx.auth.utils.UUIDUtils;
import com.zhcx.auth.vo.ApplicationBaseInfoVO;
import com.zhcx.auth.vo.ApplicationRoleBaseInfoVO;

/**
 * @ClassName：ApplicationServiceImpl
 * @Description:
 * @author：李亮
 * @date：2020/4/215:40
 */
@Service("applicationService")
public class ApplicationServiceImpl implements ApplicationService {

    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static final String APPLICATION = "Application";
    @Resource
    private UUIDUtils uuidUtils;
    @Autowired
    private ApplicationBaseInfoMapper applicationBaseInfoMapper;
    @Autowired
    private ApplicationUserBaseInfoMapper applicationUserBaseInfoMapper;
    @Autowired
    private ApplicationRoleBaseInfoMapper applicationRoleBaseInfoMapper;
    @Autowired
    private AuthMenuRelationMapper authMenuRelationMapper;
    @Autowired
    private AuthRoleMapper authRoleMapper;

    /**
     * 应用新增
     *
     * @param applicationBaseInfo
     * @return
     */
    @Override
    public ApplicationBaseInfo add(ApplicationBaseInfo applicationBaseInfo) {
        applicationBaseInfo.setUuid(uuidUtils.getLongUUID(APPLICATION));
        applicationBaseInfo.setStatus(1);
        applicationBaseInfo.setCreatorTime(sdf.format(new Date()));
        applicationBaseInfo.setModifierTime(sdf.format(new Date()));
        int i = applicationBaseInfoMapper.insertSelective(applicationBaseInfo);
        if (i <= 0) {
            return null;
        }
        return applicationBaseInfo;
    }

    /**
     * 应用列表
     *
     * @param applicationBaseInfo
     * @return
     */
    @Override
    public PageInfo<ApplicationBaseInfo> selectList(ApplicationBaseInfoVO applicationBaseInfo) {
        PageInfo<ApplicationBaseInfo> pageInfo = null;
        if (null != applicationBaseInfo.getPageNo() || null != applicationBaseInfo.getPageSize()) {
            PageHelper.startPage(applicationBaseInfo.getPageNo(), applicationBaseInfo.getPageSize());
        }
        PageHelperUtil.orderBy("creator_time_desc");
        pageInfo = new PageInfo<>(applicationBaseInfoMapper.select(applicationBaseInfo));
        return pageInfo;
    }

    /**
     * 修改
     *
     * @param applicationBaseInfoVO
     * @return
     */
    @Override
    public Integer update(ApplicationBaseInfoVO applicationBaseInfoVO) {
        ApplicationBaseInfo applicationBaseInfo = new ApplicationBaseInfo();
        BeanUtils.copyProperties(applicationBaseInfoVO, applicationBaseInfo);
        applicationBaseInfo.setModifierTime(sdf.format(new Date()));
        return applicationBaseInfoMapper.updateByPrimaryKey(applicationBaseInfo);
    }

    /**
     * 基于用户获取对应应用与角色列表
     *
     * @param applicationBaseInfoVO
     * @return
     */
    @Override
    public List<ApplicationRoleBaseInfoVO> selectByUser(ApplicationBaseInfoVO applicationBaseInfoVO) {
        List<ApplicationRoleBaseInfoVO> applicationRoleBaseInfoVO = new ArrayList<>();
        applicationRoleBaseInfoVO = applicationRoleBaseInfoMapper.selectByUser(applicationBaseInfoVO);


        return applicationRoleBaseInfoVO;
    }

    @Override
    public List<ApplicationBaseInfoVO> selectUserList(ApplicationUserBaseInfoVO applicationBaseInfoVO) {
        return applicationBaseInfoMapper.selectUserList(applicationBaseInfoVO);
    }

    /**
     * 获取账户下面所有的权限
     *
     * @param user
     * @return
     */
    @Override
    public Boolean jurisdiction(AuthUserResp user) {

        //应用权限鉴定
        ApplicationUserBaseInfo applicationUserBaseInfo = new ApplicationUserBaseInfo();
        applicationUserBaseInfo.setUserId(user.getUserId());
        applicationUserBaseInfo.setApplicationCode("00");//综合管理模块
        List<ApplicationUserBaseInfo> applicationList = applicationUserBaseInfoMapper.selectByUserId(applicationUserBaseInfo);
        if (null == applicationList && applicationList.size() <= 0) {
            return false;
        }
        //角色权限
        ApplicationRoleBaseInfoVO role = new ApplicationRoleBaseInfoVO();
        role.setUserId(user.getUserId());
        role.setApplicationCode(applicationList.get(0).getApplicationCode());
        AuthRole authRole = authRoleMapper.selectByApplication(role);
        if (authRole.getRoleType().equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<ApplicationRoleBaseInfoVO> selectApplicationAndRole(ApplicationBaseInfoVO applicationBaseInfoVO) {
        return authRoleMapper.selectApplicationAndRole(applicationBaseInfoVO).stream().filter(a -> a.getRoleId() != null).collect(Collectors.toList());
    }

    /**
     * 删除
     *
     * @param applicationBaseInfoVO
     * @return
     */
    @Override
    public Integer del(ApplicationBaseInfoVO applicationBaseInfoVO) {
        ApplicationBaseInfo applicationBaseInfo = new ApplicationBaseInfo();
        BeanUtils.copyProperties(applicationBaseInfoVO, applicationBaseInfo);
        applicationBaseInfo.setModifierTime(sdf.format(new Date()));
        ApplicationUserBaseInfoVO applicationUserBaseInfoVO = new ApplicationUserBaseInfoVO();
        applicationUserBaseInfoVO.setApplicationCode(applicationBaseInfo.getCode());
        List<ApplicationBaseInfoVO> applicationBaseInfoVOS = applicationBaseInfoMapper.selectUserList(applicationUserBaseInfoVO);
        if (applicationBaseInfoVOS != null && applicationBaseInfoVOS.size() > 0) {
            return null;
        }
        ApplicationUserBaseInfo applicationUserBaseInfo = new ApplicationUserBaseInfo();
        applicationUserBaseInfo.setApplicationCode(applicationBaseInfo.getCode());
        List<ApplicationBaseInfoVO> applicationBaseInfoVOList = applicationBaseInfoMapper.selectByRole(applicationUserBaseInfo);
        if (applicationBaseInfoVOList != null && applicationBaseInfoVOList.size() > 0) {
            return null;
        }
        return applicationBaseInfoMapper.updateByPrimaryKey(applicationBaseInfo);
    }
}
