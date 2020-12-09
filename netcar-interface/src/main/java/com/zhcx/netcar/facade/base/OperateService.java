package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.params.OperateParam;
import com.zhcx.netcar.pojo.base.*;


/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/10 21:29
 **/
public interface OperateService {
    /**
     * 查询车辆营运上线列表
     * @param param
     * @return
     */
    PageInfo<NetcarOperateLogin> queryOperateLoginList(OperateParam param) throws Exception;

    /**
     * 查询车辆营运下线列表
     * @param param
     * @return
     */
    PageInfo<NetcarOperateLogout> queryOperateLogoutList(OperateParam param) throws Exception;

    /**
     * 查询营运信息（经营出发）列表
     * @param param
     * @return
     */
    PageInfo<NetcarOperateDepart> queryOperateDepartList(OperateParam param) throws Exception;

    /**
     * 查询营运信息（经营到达）列表
     * @param param
     * @return
     */
    PageInfo<NetcarOperateArrive> queryOperateArriveList(OperateParam param) throws Exception;

    /**
     * 查询营运信息（经营支付）列表
     * @param param
     * @return
     */
    PageInfo<NetcarOperatePay> queryOperatePayList(OperateParam param) throws Exception;

    /**
     * 查询综合营运信息（出发/到达/支付）
     * @param param
     * @return
     */
    PageInfo<NetcarOperateInfo> queryOperateInfoList(OperateParam param) throws Exception;
}
