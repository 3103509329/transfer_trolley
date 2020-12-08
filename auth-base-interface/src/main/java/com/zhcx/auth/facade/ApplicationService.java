package com.zhcx.auth.facade;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.ApplicationBaseInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.auth.vo.ApplicationBaseInfoVO;
import com.zhcx.auth.vo.ApplicationRoleBaseInfoVO;
import com.zhcx.auth.vo.ApplicationUserBaseInfoVO;

import java.util.List;

public interface ApplicationService {

    /**
     * 应用新增
     * @param applicationBaseInfo
     * @return
     */
    ApplicationBaseInfo add(ApplicationBaseInfo applicationBaseInfo);

    /**
     * 应用列表查询
     * @param applicationBaseInfo
     * @return
     */
    PageInfo<ApplicationBaseInfo> selectList(ApplicationBaseInfoVO applicationBaseInfo);

    Integer update(ApplicationBaseInfoVO applicationBaseInfoVO);

    List<ApplicationRoleBaseInfoVO> selectByUser(ApplicationBaseInfoVO applicationBaseInfoVO);

    List<ApplicationBaseInfoVO> selectUserList(ApplicationUserBaseInfoVO applicationBaseInfoVO);

    Boolean jurisdiction(AuthUserResp user);

    List<ApplicationRoleBaseInfoVO> selectApplicationAndRole(ApplicationBaseInfoVO applicationBaseInfoVO);

    Integer del(ApplicationBaseInfoVO applicationBaseInfoVO);
}
