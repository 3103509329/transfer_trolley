package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompany;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoVehicle;
import com.zhcx.netcar.vo.BaseInfoVehiclelicence;

import java.util.List;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 13:55
 */
public interface VehicleService {


    /**
     * 车辆信息查询
     * @param
     * @return
     */
    PageInfo<NetcarBaseInfoVehicle> selectVehicleList(String companyId, String vehicleNo, Integer pageNo, Integer pageSize, String orderBy) throws Exception;

    /**
     * 车辆和关联企业信息查询
     * @param
     * @return
     */
    NetcarBaseInfoVehicle selectVehicleInfo(String companyId, String vehicleNo) throws Exception;


    /**
     * 根据公司ID查询公司所有车辆
     * @param corpIds
     * @return
     */
    List<NetcarBaseInfoVehicle> queryCompanyVehicleList(List<String> corpIds) throws Exception;

    /**
     * 根据公司ID查询车辆车牌
     * @param companyId
     * @return
     */
    List<String> selectVehicleNoListByCompanyId(String companyId);


    /**
     * 根据区划代码查询车辆信息
     * @param address
     * @return
     */
    List<NetcarBaseInfoVehicle> queryVehicleListByAddress(String address);

    /**
     * 查询行政区划代码
     * @param companyId
     * @param vehicleNo
     * @return
     */
    Integer selectAddressByCompanyIdAndVehicleNo(String companyId, String vehicleNo);

    /**
     * 查询不合规车辆
     * @param companyId
     * @param vehicleNo
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    PageInfo<NetcarBaseInfoVehicle> selectIllegalVehicleList(String companyId, String vehicleNo, Integer pageNo, Integer pageSize, String orderBy);

    /**
     * 获取违规车辆（工作台）
     * @param companyId
     * @param vehicleNo
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    PageInfo<NetcarBaseInfoVehicle> getVehilceIllegal(String companyId, String vehicleNo, Integer pageNo, Integer pageSize, String orderBy);

    /**
     * 查询违规企业List或指定违规企业
     * @param companyId
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    PageInfo<NetcarBaseInfoCompany> getCompanyList(String companyId, Integer pageNo, Integer pageSize, String orderBy);

    /**
     * 获取违规企业（所有）
     * @return
     */
    PageInfo<NetcarBaseInfoCompany> getCompanys();

    void insertBatchBaseVehicle(List<BaseInfoVehiclelicence> insertBaseVehicleList);

    void updateBatchBaseVehicle(List<BaseInfoVehiclelicence> updateBaseVehicelList);
}
