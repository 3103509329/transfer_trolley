package com.zhcx.netcar.facade.app;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.app.NetcarAppTrafficAssessment;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/5/18 15:23
 **/
public interface TrafficAssessmentService {
    /**
     * 查询记录列表
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @param keyword
     * @param branum
     * @return
     */
    PageInfo<NetcarAppTrafficAssessment> queryTrafficAssessmentList(Integer pageNo, Integer pageSize, String orderBy, String keyword, String branum) throws Exception;

    /**
     * 保存记录
     * @param param
     * @return
     */
    int insertTrafficAssessment(NetcarAppTrafficAssessment param) throws Exception;
}
