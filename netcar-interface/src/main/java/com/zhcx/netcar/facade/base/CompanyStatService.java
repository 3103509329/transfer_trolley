package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompanyStat;

import java.util.List;
import java.util.Map;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 13:54
 */
public interface CompanyStatService {

    /**
     * 平台公司营运规模信息查询
     *
     * @param
     * @return
     */
    PageInfo<NetcarBaseInfoCompanyStat> selectCompanyStatList(String companyId, Integer pageNo, Integer pageSize, String orderBy) throws Exception;

    List<Map<String, Object>> getCountByYear(String substring, String companyId, String corpId) throws Exception;

    List<Map<String, Object>> getCarSum(String corpId, String startTime, String type, String companyId) throws Exception;

    List<Map<String, Object>> getDriverSum(String corpId, String startTime, String type, String companyId) throws Exception;
}

