package com.zhcx.auth.facade;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.ApplicationRoleBaseInfo;
import com.zhcx.auth.vo.ApplicationBaseInfoVO;
import com.zhcx.auth.vo.ApplicationRoleBaseInfoVO;

import java.util.List;

public interface ApplicationRoleService {
    Boolean add(ApplicationRoleBaseInfoVO applicationRoleBaseInfoVO);

    Integer update(List<ApplicationBaseInfoVO> applicationBaseInfoList);

    PageInfo<ApplicationRoleBaseInfo> select(ApplicationRoleBaseInfoVO applicationRoleBaseInfoVO);

    Integer delete(ApplicationRoleBaseInfoVO applicationRoleBaseInfoVO);

//    PageInfo<ApplicationBaseInfoVO> selectByRole(AuthUserResp authUser);
}
