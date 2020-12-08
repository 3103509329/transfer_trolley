package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarOperateLogin;
import com.zhcx.netcar.pojo.base.NetcarRatedPassenger;
import com.zhcx.netcar.params.QueryParam;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/11/27 16:32
 **/
public interface RatedPassengerService {
    /**
     * 查询乘客评价信息
     *
     * @param param
     * @return
     * @throws Exception
     */
    PageInfo<NetcarRatedPassenger> queryPassengerRatedList(QueryParam param) throws Exception;

    /**
     * 查询乘客评价信息（基于车牌）
     *
     * @param param
     * @return
     */
    PageInfo<NetcarRatedPassenger> queryPassengerRatedVehicleNo(QueryParam param);
}
