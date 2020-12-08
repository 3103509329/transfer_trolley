package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompany;
import com.zhcx.netcar.facade.base.CompanyService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarBaseInfoCompanyMapper;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarBaseInfoCompanyServiceMapper;
import com.zhcx.netcar.netcarservice.utils.CompanyUtils;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import com.zhcx.netcar.vo.CompanyNameVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 14:00
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private NetcarBaseInfoCompanyMapper netcarBaseInfoCompanyMapper;

    @Autowired
    private CompanyUtils companyUtils;

    @Autowired
    private NetcarBaseInfoCompanyServiceMapper netcarBaseInfoCompanyServiceMapper;
    /**
     * 平台基本信息获取
     *
     * @param companyId
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     * @throws Exception
     */
    @Override
    public PageInfo<NetcarBaseInfoCompany> selectCompanyList(String companyId, Integer pageNo, Integer pageSize, String orderBy) throws Exception {

        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoCompany> netcarBaseInfoCompanyList = netcarBaseInfoCompanyMapper.selectCompanyList(companyId);
        return new PageInfo<>(netcarBaseInfoCompanyList);
    }

    @Override
    public List<Map<String, Object>> getCompanyByAddress(Integer address) throws Exception{
        List<Map<String, Object>> netcarBaseInfoCompanies = netcarBaseInfoCompanyMapper.getCompanyByAddress(address);
        return netcarBaseInfoCompanies;
    }



    /**
     * 平台公司信息获取（所有的公司）
     *
     * @param
     * @return
     */
    @Override
    public PageInfo<CompanyNameVo> selectCompanyIdAndName(String companyId , String keyword) throws Exception {

//        List<NetcarBaseInfoCompany> netcarBaseInfoCompanyList = netcarBaseInfoCompanyMapper.selectCompanyIdAndName(companyId, keyword);
        List<CompanyNameVo> netcarBaseInfoCompanyList = netcarBaseInfoCompanyServiceMapper.selectCompanyNameInYunzhengByCompanyId(companyId, keyword);
        return new PageInfo<>(netcarBaseInfoCompanyList);
    }


    @Override
    public NetcarBaseInfoCompany selectByCompanyId(String companyId) throws Exception {
        NetcarBaseInfoCompany netcarBaseInfoCompany = netcarBaseInfoCompanyMapper.selectByCompanyId(companyId);
        //添加企业名称
        if (null != netcarBaseInfoCompany){
            netcarBaseInfoCompany = (NetcarBaseInfoCompany) companyUtils.addCompanyName(netcarBaseInfoCompany);
        }
        return netcarBaseInfoCompany;
    }


    @Override
    public List<NetcarBaseInfoCompany> queryCompanyListByIds(Set<String> corpIds) {
        return netcarBaseInfoCompanyMapper.queryCompanyListByIds(corpIds);
    }

//    @Override
//    public PageInfo<CompanyExcel> selectCompanyExcelList(String companyId, Integer pageNo, Integer pageSize, String orderBy) throws Exception {
//        PageHelper.startPage(pageNo, pageSize);
//        PageHelperUtil.orderBy(orderBy);
//        List<CompanyExcel> list = netcarBaseInfoCompanyMapper.selectCompanyExcelList(companyId);
//        return new PageInfo<>(list);
//    }


}
