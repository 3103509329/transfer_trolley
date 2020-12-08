package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoDriverApp;
import com.zhcx.netcar.facade.base.CompanyServiceService;
import com.zhcx.netcar.facade.base.DriverAppService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarBaseInfoDriverAppMapper;
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
public class DriverAppServiceImpl implements DriverAppService {

    @Autowired
    private NetcarBaseInfoDriverAppMapper netcarBaseInfoDriverAppMapper;

    @Autowired
    private CompanyServiceService companyServiceService;

    @Override
    public PageInfo<NetcarBaseInfoDriverApp> selectDriverAppList(String companyId, String keyword, Integer pageNo, Integer pageSize, String orderBy) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoDriverApp> list = netcarBaseInfoDriverAppMapper.selectDriverAppList(companyId, keyword);
        list.forEach(item -> {
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<NetcarBaseInfoDriverApp> selectDriverAppByLicenseId(String companyId, String licenseId, Integer pageNo, Integer pageSize, String orderBy) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoDriverApp> list = netcarBaseInfoDriverAppMapper.selectDriverAppByLicenseId(companyId, licenseId);
        list.forEach(item -> {
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }
}
