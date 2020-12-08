package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoVehicleTotalMile;
/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 13:56
 */
public interface VehicleTotalMileService {

    /**
     * 车辆里程信息查询
     *
     * @param
     * @return
     */
    PageInfo<NetcarBaseInfoVehicleTotalMile> selectVehicleTotalMileList(String companyId, String vehicleNo, Integer pageNo, Integer pageSize, String orderBy) throws Exception;

    /**
     * 根据车牌号查询车辆里程信息
     *
     * @param companyId
     * @param vehicleNo
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    PageInfo<NetcarBaseInfoVehicleTotalMile> selectVehicleTotalMileByVehicleNo(String companyId, String vehicleNo, Integer pageNo, Integer pageSize, String orderBy) throws Exception;
}
