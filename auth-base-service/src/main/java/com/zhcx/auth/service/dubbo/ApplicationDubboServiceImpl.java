package com.zhcx.auth.service.dubbo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.dubbo.ApplicationDubboService;
import com.zhcx.auth.mapper.ApplicationBaseInfoMapper;
import com.zhcx.auth.mapper.ApplicationRoleBaseInfoMapper;
import com.zhcx.auth.mapper.ApplicationUserBaseInfoMapper;
import com.zhcx.auth.mapper.AuthMenuRelationMapper;
import com.zhcx.auth.pojo.ApplicationBaseInfo;
import com.zhcx.auth.pojo.ApplicationUserBaseInfo;
import com.zhcx.auth.pojo.AuthMenuRelation;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.auth.utils.UUIDUtils;
import com.zhcx.auth.vo.ApplicationBaseInfoVO;
import com.zhcx.auth.vo.ApplicationRoleBaseInfoVO;

/**
 * @ClassName：ApplicationServiceImpl
 * @Description:
 * @author：李亮
 * @date：2020/4/215:40
 */
@Service(version = "${zhcx.business.dubbo.version}",interfaceClass = ApplicationDubboService.class)
public class ApplicationDubboServiceImpl implements ApplicationDubboService {

    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static final String APPLICATION = "Application";
    @Autowired
    private UUIDUtils uuidUtils;
    @Autowired
    private ApplicationBaseInfoMapper applicationBaseInfoMapper;
    @Autowired
    private ApplicationUserBaseInfoMapper applicationUserBaseInfoMapper;
    @Autowired
    private ApplicationRoleBaseInfoMapper applicationRoleBaseInfoMapper;
    @Autowired
    private AuthMenuRelationMapper authMenuRelationMapper;

    /**
     * 应用新增
     *
     * @param applicationBaseInfo
     * @return
     */
    @Override
    public ApplicationBaseInfo add(ApplicationBaseInfo applicationBaseInfo) {
        applicationBaseInfo.setUuid(uuidUtils.getLongUUID(APPLICATION));
        applicationBaseInfo.setCreatorTime(sdf.format(new Date()));
        applicationBaseInfo.setModifierTime(sdf.format(new Date()));
        applicationBaseInfo.setStatus(1);
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
        PageHelper.startPage(applicationBaseInfo.getPageNo(), applicationBaseInfo.getPageSize());
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
        return applicationBaseInfoMapper.updateByPrimaryKey(applicationBaseInfo);
    }

    /**
     * 基于用户获取对应应用与角色列表
     *
     * @param applicationBaseInfoVO
     * @return
     */
    @Override
    public List<ApplicationBaseInfoVO> selectByUser(ApplicationBaseInfoVO applicationBaseInfoVO) {
        List<ApplicationBaseInfoVO> applicationBaseInfoVOList = new ArrayList<>();
        applicationBaseInfoVOList = applicationBaseInfoMapper.selectByUser(applicationBaseInfoVO);
        if (applicationBaseInfoVOList.size() <= 0) {
            return null;
        }
        List<ApplicationRoleBaseInfoVO> applicationRoleBaseInfoVOS = new ArrayList<>();
        List<String> codeList = applicationBaseInfoVOList.stream().map(ApplicationBaseInfoVO::getCode).collect(Collectors.toList());
        applicationBaseInfoVO.setCodeList(codeList);
        applicationRoleBaseInfoVOS = applicationRoleBaseInfoMapper.selectByUser(applicationBaseInfoVO);
        Map<String, List<ApplicationRoleBaseInfoVO>> map = applicationRoleBaseInfoVOS.stream().collect(Collectors.groupingBy(ApplicationRoleBaseInfoVO::getApplicationCode));
        applicationBaseInfoVOList.forEach(a -> a.setRoleList(map.get(a.getCode())));
        return applicationBaseInfoVOList;
    }

    @Override
    public List<ApplicationBaseInfoVO> selectUserList(ApplicationBaseInfoVO applicationBaseInfoVO) {
        return null;
    }

    /**
     * 获取账户下面所有的权限
     *
     * @param user
     * @return
     */
    @Override
    public Boolean jurisdiction(AuthUserResp user, String type) {

        //应用权限鉴定
        AtomicReference<Boolean> result = null;
        ApplicationUserBaseInfo applicationUserBaseInfo = new ApplicationUserBaseInfo();
        applicationUserBaseInfo.setUserId(user.getUserId());
        List<ApplicationUserBaseInfo> applicationList = applicationUserBaseInfoMapper.selectByUserId(applicationUserBaseInfo);
        if (null == applicationList && applicationList.size() <= 0) {
            result.set(false);
        }
        //菜单权限鉴定
        List<AuthMenuRelation> menu = applicationBaseInfoMapper.jurisdiction(user);
        if (null == menu && menu.size() <= 0) {
            result.set(false);
        }
        menu.forEach(a -> {
            if (a.getMenuIds().contains(type)) {
                result.set(true);
                return;
            }
        });
        return result.get();
    }
}
