package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarShareCompany;
import com.zhcx.netcar.vo.CompanyNameVo;

import java.util.List;
import java.util.Map;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/7 16:29
 **/
public interface ShareCompanyService {
    /**
     * 根据条件查询合乘公司信息
     * @param companyId
     * @param identifier
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    PageInfo<NetcarShareCompany> queryShareCompanyList(String companyId, String identifier, Integer pageNo, Integer pageSize, String orderBy);

    /**
     * 查询公司名称和id
     * @return
     */
    Map<String,String> queryNameAndId();

    /**
     * 查询公司名称列表
     * @param companyId
     * @return
     */
    List<CompanyNameVo> queryShareCompanyNameList(String companyId);

    /**
     * 根据companyId和address查询公司名称
     * @param companyId
     * @param address
     * @return
     */
    String selectCompanyNameByCompanyIdAndAddress(String companyId, Integer address);
}
