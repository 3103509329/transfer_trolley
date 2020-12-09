package com.zhcx.netcar.facade.base;


import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarRatedDriverPunish;
import com.zhcx.netcar.params.RatedDriverParam;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/11/28 9:56
 **/
public interface RatedDriverPunishService {
    /**
     * 查询驾驶员惩罚信息
     * @param param
     * @return
     * @throws Exception
     */
    PageInfo<NetcarRatedDriverPunish> queryDriverPunish(RatedDriverParam param) throws Exception;

}
