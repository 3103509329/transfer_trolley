package com.zhcx.netcar.facade.workbench;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.workbech.WorkbenchDriver;
import com.zhcx.netcar.pojo.workbech.WorkbenchVehicle;
import com.zhcx.netcar.vo.*;


/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-15 15:30
 * 工作台
 */
public interface WorkbenchService {

    /**
     * 益阳网约车车辆监管情况
     * @return
     * @throws Exception
     */
    PageInfo<WorkbenchVehicle> getVehicleMonitoring(int pageNo, int pageSize) throws Exception;

    /**
     * 益阳网约车驾驶员监管情况
     * @return
     * @throws Exception
     */
    PageInfo<WorkbenchDriver> getDriverMonitoring(int pageNo, int pageSize) throws Exception;

    /**
     * 年检过期
     * @param busiRegNumber
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<VehicleAnnualInspectionExpired> getAnnualInspectionOverdue(String busiRegNumber, int pageNo, int pageSize)throws Exception;

    /**
     * 无车载终端
     * @param companyId
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<VehicleNotTerminal> getNotCarTerminal(String busiRegNumber, String companyId, int pageNo, int pageSize)throws Exception;

    /**
     * 保险金额不合格
     * @param busiRegNumber
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<VehicleInsurExpired> getInsuredAmountUnqualified(String busiRegNumber, int pageNo, int pageSize)throws Exception;

    /**
     * 保险到期
     * @param busiRegNumber
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<VehicleInsurExpired> getInsuranceDue(String busiRegNumber, int pageNo, int pageSize)throws Exception;

    /**
     * 里程超限
     * @param busiRegNumber
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<VehicleMileageBeyond> getMileageTransfinite(String busiRegNumber, int pageNo, int pageSize)throws Exception;

    /**
     * 未运营
     * @param companyId
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<VehicleNotOperating> getNotOperating(String busiRegNumber, String companyId, int pageNo, int pageSize)throws Exception;

    /**
     * 数据缺失车辆
     * @param busiRegNumber
     * @param companyId
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<VehicleInfoLack> getLackData(String busiRegNumber, String companyId, int pageNo, int pageSize)throws Exception;

    /**
     * 驾照过期
     * @param busiRegNumber
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<DriverLicenseExpired> getLicenseOverdue(String busiRegNumber, int pageNo, int pageSize)throws Exception;

    /**
     * 年龄超标
     * @param busiRegNumber
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<DriverAgeBeyond> getAgeExcessive(String busiRegNumber, int pageNo, int pageSize)throws Exception ;

    /**
     * 持证未上岗
     * @param busiRegNumber
     * @param companyId
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<DriverNotOperating> getNotMountGuard(String busiRegNumber, String companyId, int pageNo, int pageSize)throws Exception;

    /**
     * 数据缺失
     * @param busiRegNumber
     * @param companyId
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<DriverInfoLack> getDriverLackData(String busiRegNumber, String companyId, int pageNo, int pageSize);

    /**
     * 更新数据
     * @return
     */
    void update() throws Exception;
}
