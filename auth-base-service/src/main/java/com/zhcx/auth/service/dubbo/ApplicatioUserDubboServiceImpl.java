package com.zhcx.auth.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhcx.auth.dubbo.ApplicationUserDubboService;
import com.zhcx.auth.mapper.ApplicationUserBaseInfoMapper;
import com.zhcx.auth.pojo.ApplicationRoleBaseInfo;
import com.zhcx.auth.pojo.ApplicationUserBaseInfo;
import com.zhcx.auth.vo.ApplicationBaseInfoVO;
import com.zhcx.auth.vo.ApplicationUserBaseInfoVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName：ApplicatioUserServiceImpl
 * @Description:
 * @author：李亮
 * @date：2020/4/916:38
 */
@Service(version = "${zhcx.business.dubbo.version}",interfaceClass = ApplicationUserDubboService.class)
public class ApplicatioUserDubboServiceImpl implements ApplicationUserDubboService {



    @Autowired
    private ApplicationUserBaseInfoMapper applicationUserBaseInfoMapper;

    @Override
    public List<ApplicationRoleBaseInfo> add(ApplicationUserBaseInfoVO applicationRoleBaseInfoVO) {
        return null;
    }

    /**
     * 基于用户获取权限下所有的企业
     *
     * @param applicationUserBaseInfo
     * @return
     */
    @Override
    public List<ApplicationBaseInfoVO> selectByPermissions(ApplicationUserBaseInfo applicationUserBaseInfo) {
        return applicationUserBaseInfoMapper.selectByPermissions(applicationUserBaseInfo);
    }
}
