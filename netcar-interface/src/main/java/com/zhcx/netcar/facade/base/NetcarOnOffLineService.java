package com.zhcx.netcar.facade.base;


/**
 * @autor ht(15616537979@126.com)
 * @date 2018/12/27
 * @version V1.0
 * @description
 *
**/

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.params.QueryParam;
import com.zhcx.netcar.pojo.base.NetcarOnOffLine;
import com.zhcx.netcar.pojo.base.NetcarOperateInfo;
import com.zhcx.netcar.pojo.base.NetcarOperateLogin;
import com.zhcx.netcar.pojo.base.NetcarOperateLogout;

import java.util.List;

/**
 * 获取车辆上下线接口
 */
public interface  NetcarOnOffLineService{

    /**
     * 在离线车辆列表
     * @param vehicleNos
     * @return
     */
    List<NetcarOnOffLine> getVehicleOnOffLineList(String vehicleNos) throws Exception;

    void deleteLogIn(NetcarOperateLogin netcarOperateInfo);

    void insertLogIn(NetcarOperateLogin netcarOperateInfo);

    void deleteLogOut(NetcarOperateLogout netcarOperateInfo);

    void insertLogOut(NetcarOperateLogout netcarOperateInfo);

    PageInfo<NetcarOperateLogin> queryloginoutVehicleNo(QueryParam param);
}
