package com.zhcx.netcar.facade.base;


import com.zhcx.netcar.params.VehiclePositionParam;
import com.zhcx.netcar.pojo.base.NetcarPositionDriver;
import com.zhcx.netcar.pojo.base.NetcarPositionVehicle;

import java.text.ParseException;
import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/12/19 14:13
 **/
public interface PositionService {

    /**
     * 查询历史记录
     * @param
     * @return
     */
    List<NetcarPositionVehicle> queryHistoryPosition(String vehicleNo, Long ts) throws Exception;

    /**
     * 查询车辆实时定位
     */
    NetcarPositionVehicle queryRealTimePosition(String vehicleNo) throws Exception;

    /**
     * 查询驾驶员定位信息
     * @param licenseId
     * @param ts
     * @return
     */
    List<NetcarPositionDriver> queryDriverPosition(String licenseId, Long ts);

    /**
     * 查询历史轨迹（通过redis实现）
     * @param vehicleNo
     * @param ts
     * @return
     */
    int selectList(String vehicleNo, Long ts) throws ParseException;

    List queryOrderHistoryPosition(VehiclePositionParam vehiclePositionParam);
}
