package com.zhcx.netcar.facade.app;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.app.NetcarPermitVehicleInfo;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/2/22 17:45
 **/
public interface PermitVehicleService {

    /**
     * 车辆许可信息列表查询
     * @param corpId
     * @param keyword
     * @param flag
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    PageInfo<NetcarPermitVehicleInfo> selectPermitVehicleInfoList(Long corpId, String keyword, Integer flag, Integer pageNo, Integer pageSize, String orderBy) throws Exception;

    /**
     * 车辆许可信息新增
     * @param netcarPermitVehicleInfo
     * @return
     */
    int insertPermitVehicleInfo(NetcarPermitVehicleInfo netcarPermitVehicleInfo) throws Exception;

    /**
     * 车辆许可信息更新
     * @param netcarPermitVehicleInfo
     * @return
     */
    int updatePermitVehicleInfo(NetcarPermitVehicleInfo netcarPermitVehicleInfo) throws Exception;

    /**
     * 车辆许可信息审核
     * @param uuid
     * @param flag
     * @param reason
     * @return
     */
    int updatePermitVehicleInfoStatus(Long uuid, Integer flag, String reason);
}
