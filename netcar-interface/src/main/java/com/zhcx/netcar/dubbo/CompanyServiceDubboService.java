package com.zhcx.netcar.dubbo;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompanyService;
import com.zhcx.netcar.vo.CompanyNameVo;

import java.util.List;
import java.util.Set;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 13:53
 */
public interface CompanyServiceDubboService {

    /**
     * 平台公司服务机构查询
     * @param
     * @return
     */
    PageInfo<NetcarBaseInfoCompanyService> selectCompanyServiceList(String companyId, Integer pageNo, Integer pageSize, String orderBy) throws Exception;

    /**
     * 查询公司（服务机构名称）
     * @param companyId
     * @return
     */
    String selectCompanyNameByCompanyIdAndAddress(String companyId, Integer address);

    /**
     * 查询所有公司的标识
     * @return
     */
    List<String> selectAllCompanyIdList();

    /**
     * 基于运政企业uuid查询部级企业数据
     * @param companyId
     * @return
     */
    String selectCompanyIdByYZuuid(Long companyId) throws Exception;

    /**
     * 获取指定公司的名称
     * @param key
     * @return
     */
    String selectCompanyNameByCompanyId(String key);


    /**
     * 企业信息查询
     * @param
     * @return
     */
    NetcarBaseInfoCompanyService selectByCompanyId(String companyId) ;

    /**
     * 获取集合
     * @param corpIds
     * @return
     */
    List<NetcarBaseInfoCompanyService> queryCompanyListByIds(Set<String> corpIds);

    /**
     * 查询不合规企业
     * @param companyId
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    PageInfo<NetcarBaseInfoCompanyService> selectIllegalCompanyServiceList(String companyId, Integer pageNo, Integer pageSize, String orderBy);

    /**
     * 查询服务机构名称
     * @param companyId
     * @param keyword
     * @return
     */
    List<CompanyNameVo> selectCompanyIdAndName(String companyId, String keyword);

    /**
     * 查询服务机构名称（基于围栏ID，超区域经营电子围栏专用）
     * @param regionId
     * @return
     */
    String selectCompanyNameByRegionId(Long regionId);
}
