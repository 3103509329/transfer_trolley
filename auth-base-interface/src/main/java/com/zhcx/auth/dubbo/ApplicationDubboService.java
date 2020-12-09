package com.zhcx.auth.dubbo;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.ApplicationBaseInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.auth.vo.ApplicationBaseInfoVO;

import java.util.List;

public interface ApplicationDubboService {

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

    List<ApplicationBaseInfoVO> selectByUser(ApplicationBaseInfoVO applicationBaseInfoVO);

    List<ApplicationBaseInfoVO> selectUserList(ApplicationBaseInfoVO applicationBaseInfoVO);

    Boolean jurisdiction(AuthUserResp user, String type);

}
