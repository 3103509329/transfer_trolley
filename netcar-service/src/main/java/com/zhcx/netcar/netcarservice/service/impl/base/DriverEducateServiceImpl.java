package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoDriverEducate;
import com.zhcx.netcar.facade.base.CompanyServiceService;
import com.zhcx.netcar.facade.base.DriverEducateService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarBaseInfoDriverEducateMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 14:01
 */
@Service
public class DriverEducateServiceImpl implements DriverEducateService {


    @Autowired
    private NetcarBaseInfoDriverEducateMapper netcarBaseInfoDriverEducateMapper;

    @Autowired
    private CompanyServiceService companyServiceService;

    @Override
    public PageInfo<NetcarBaseInfoDriverEducate> selectDriverEducateList(String companyId, String licenseId, Integer pageNo, Integer pageSize, String orderBy) throws Exception{

        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoDriverEducate> list = netcarBaseInfoDriverEducateMapper.selectDriverEducateList(companyId, licenseId);
        //企业名称添加
        list.forEach(item -> {
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<NetcarBaseInfoDriverEducate> selectDriverEducateByLicenseId(String companyId, String licenseId, Integer pageNo, Integer pageSize, String orderBy) throws Exception{
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoDriverEducate> list = netcarBaseInfoDriverEducateMapper.selectDriverEducateByLicenseId(companyId, licenseId);
        //企业名称添加
        list.forEach(item -> {
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }


}
