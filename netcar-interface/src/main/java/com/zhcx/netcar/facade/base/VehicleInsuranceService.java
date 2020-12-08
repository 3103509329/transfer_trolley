package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoVehicleInsurance;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 13:55
 */
public interface VehicleInsuranceService {

    /**
     * 车辆保险信息查询
     * @param
     * @return
     */
    PageInfo<NetcarBaseInfoVehicleInsurance> selectVehicleInsuranceList(String companyId, String vehicleNo, Integer pageNo, Integer pageSize, String orderBy) throws Exception;

    /**
     * 根据车辆号牌查询车辆保险信息
     * @param companyId
     * @param vehicleNo
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    PageInfo<NetcarBaseInfoVehicleInsurance> selectVehicleInsuranceByVehicleNo(String companyId, String vehicleNo, Integer pageNo, Integer pageSize, String orderBy)throws Exception;
}
