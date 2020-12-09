package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompanyFare;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 13:51
 */
public interface CompanyFareService {

    /**
     * 平台公司运价信息查询
     * @param
     * @return
     */

    PageInfo<NetcarBaseInfoCompanyFare> selectCompanyFareList(String companyId, Integer pageNo, Integer pageSize, String orderBy)throws Exception;
}
