package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoPassenger;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/7 10:33
 **/
public interface PassengerService {
    /**
     * 查询乘客信息
     * @param companyId
     * @param passengerPhone
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    PageInfo<NetcarBaseInfoPassenger> queryPassengerList(String companyId, String passengerPhone, Integer pageNo, Integer pageSize, String orderBy) throws Exception;
}
