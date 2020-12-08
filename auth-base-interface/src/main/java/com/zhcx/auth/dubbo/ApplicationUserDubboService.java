package com.zhcx.auth.dubbo;

import com.zhcx.auth.pojo.ApplicationRoleBaseInfo;
import com.zhcx.auth.pojo.ApplicationUserBaseInfo;
import com.zhcx.auth.vo.ApplicationBaseInfoVO;
import com.zhcx.auth.vo.ApplicationUserBaseInfoVO;

import java.util.List;

public interface ApplicationUserDubboService {

    List<ApplicationRoleBaseInfo> add(ApplicationUserBaseInfoVO applicationRoleBaseInfoVO);

    List<ApplicationBaseInfoVO> selectByPermissions(ApplicationUserBaseInfo applicationUserBaseInfo);
}
