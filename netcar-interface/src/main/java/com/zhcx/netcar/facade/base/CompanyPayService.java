package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompanyPay;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 13:52
 */
public interface CompanyPayService {

    /**
     * 平台公司支付信息查询
     * @param
     * @return
     */
    PageInfo<NetcarBaseInfoCompanyPay> getCompanyPayList(String companyId, Integer pageNo, Integer pageSize, String orderBy) throws Exception;

}
