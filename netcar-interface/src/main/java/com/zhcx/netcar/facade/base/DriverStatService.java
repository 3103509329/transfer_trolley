package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoDriverStat;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 13:55
 */
public interface DriverStatService {

    /**
     * 驾驶员统计信息查询
     * @param
     * @return
     */
    PageInfo<NetcarBaseInfoDriverStat> selectDriverStatList(String companyId, String driverName, String driverPhone, String keyword, Integer pageNo, Integer pageSize, String orderBy)throws Exception;

    /**
     * 根据驾驶证号查询驾驶员统计信息
     * @param companyId
     * @param licenseId
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    PageInfo<NetcarBaseInfoDriverStat> selectDriverStatByLicenseId(String companyId, String licenseId, Integer pageNo, Integer pageSize, String orderBy)throws Exception;
}
