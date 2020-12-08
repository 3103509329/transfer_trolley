package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoDriverApp;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 13:54
 */
public interface DriverAppService {

    /**
     * 驾驶员移动终端信息查询
     *
     * @param
     * @return
     */
    PageInfo<NetcarBaseInfoDriverApp> selectDriverAppList(String companyId, String keyword, Integer pageNo, Integer pageSize, String orderBy) throws Exception;

    /**
     * 根据驾驶证号查询移动终端信息
     *
     * @param companyId
     * @param licenseId
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    PageInfo<NetcarBaseInfoDriverApp> selectDriverAppByLicenseId(String companyId, String licenseId, Integer pageNo, Integer pageSize, String orderBy) throws Exception;
}
