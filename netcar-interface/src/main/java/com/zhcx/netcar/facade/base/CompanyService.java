package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompany;
import com.zhcx.netcar.vo.CompanyNameVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 13:51
 */
public interface CompanyService {


    /**
     * 获取所有公司ID和名称
     * @return
     */
    PageInfo<CompanyNameVo> selectCompanyIdAndName(String companyId, String keyword) throws Exception;


    /**
     * 企业信息查询
     * @param
     * @return
     */
    NetcarBaseInfoCompany selectByCompanyId(String companyId) throws Exception;


    /**
     * 平台基本信息查询
     * @param
     * @return
     */
    PageInfo<NetcarBaseInfoCompany> selectCompanyList(String companyId, Integer pageNo, Integer pageSize, String orderBy) throws Exception;


    /**
     *
     * 根据区划代码获取所有公司
     * @param addresss
     * @return
     */
    List<Map<String,Object>> getCompanyByAddress(Integer addresss)throws Exception;


    /**
     * 根据公司ID
     * @param corpIds
     * @return
     */
    List<NetcarBaseInfoCompany> queryCompanyListByIds(Set<String> corpIds);



}
