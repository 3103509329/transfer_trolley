package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarRatedDriver;
import com.zhcx.netcar.params.RatedDriverParam;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/11/28 10:52
 **/
public interface RatedDriverService {
    /**
     * 查询驾驶员信誉信息
     * @param param
     * @return
     * @throws Exception
     */
    PageInfo<NetcarRatedDriver> queryDriver(RatedDriverParam param) throws Exception;

}
