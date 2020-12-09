package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompanyPermit;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 13:53
 */
public interface CompanyPermitService {

    /**
     * 公司经营许可信息查询
     * @param
     * @return
     */
    PageInfo<NetcarBaseInfoCompanyPermit> getCompanyPermitList(String companyId, Integer pageNo, Integer pageSize, String orderBy) throws Exception;

}
