package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoDriver;
import com.zhcx.netcar.vo.BaseInfoEmpl;

import java.util.List;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 13:54
 */
public interface DriverService {

    /**
     * 驾驶员基本信息获取列表
     * @param
     * @return
     */
    PageInfo<NetcarBaseInfoDriver> selectDriverList(String companyId, String type, String keyword, Integer pageNo, Integer pageSize, String orderBy) throws Exception;

    /**
     * 查询驾驶员详细信息
     * @param
     * @return
     */
    NetcarBaseInfoDriver selectDriverInfoByCompanyId(String companyId, String licenseId) throws Exception;


    /**
     * 根据公司ID查询所有驾驶证号
     * @return
     */
    List queryLicenseIdListByCompanyId(String companyId);

    /**
     * 查询行政代码
     * @param companyId
     * @param licenseId
     * @return
     */
    Integer selectAddressByCompanyIdAndLicenseId(String companyId, String licenseId);

    void saveBatchEmpl(List<BaseInfoEmpl> insertEmplList);

    void updateBatchEmpl(List<BaseInfoEmpl> updateEmplList);
}
