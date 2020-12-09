package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoDriverEducate;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 13:54
 */
public interface DriverEducateService {

    /**
     * 驾驶员培训信息查询
     * @param
     * @return
     */
    PageInfo<NetcarBaseInfoDriverEducate> selectDriverEducateList(String companyId, String licenseId, Integer pageNo, Integer pageSize, String orderBy) throws Exception;

    /**
     *
     * @param companyId
     * @param licenseId
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    PageInfo<NetcarBaseInfoDriverEducate> selectDriverEducateByLicenseId(String companyId, String licenseId, Integer pageNo, Integer pageSize, String orderBy)throws Exception;
}
