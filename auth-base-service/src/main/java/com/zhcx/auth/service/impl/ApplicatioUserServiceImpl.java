package com.zhcx.auth.service.impl;

import com.zhcx.auth.facade.ApplicationUserService;
import com.zhcx.auth.pojo.ApplicationRoleBaseInfo;
import com.zhcx.auth.pojo.ApplicationUserBaseInfo;
import com.zhcx.auth.vo.ApplicationBaseInfoVO;
import com.zhcx.auth.vo.ApplicationUserBaseInfoVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName：ApplicatioUserServiceImpl
 * @Description:
 * @author：李亮
 * @date：2020/4/916:38
 */
@Service("applicationUserService")
public class ApplicatioUserServiceImpl implements ApplicationUserService {

    @Override
    public List<ApplicationRoleBaseInfo> add(ApplicationUserBaseInfoVO applicationRoleBaseInfoVO) {

        return null;
    }

    @Override
    public List<ApplicationBaseInfoVO> selectByPermissions(ApplicationUserBaseInfo applicationUserBaseInfo) {
        return null;
    }
}
