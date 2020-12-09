package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoDriverStat;
import com.zhcx.netcar.facade.base.CompanyServiceService;
import com.zhcx.netcar.facade.base.DriverStatService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarBaseInfoDriverStatMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 14:07
 * 驾驶员统计
 */
@Service
public class DriverStatServiceImpl implements DriverStatService {

    @Autowired
    private NetcarBaseInfoDriverStatMapper netcarBaseInfoDriverStatMapper;

    @Autowired
    private CompanyServiceService companyServiceService;

    @Override
    public PageInfo<NetcarBaseInfoDriverStat> selectDriverStatList(String companyId,
                                                                   String driverName,
                                                                   String driverPhone,
                                                                   String keyword,
                                                                   Integer pageNo,
                                                                   Integer pageSize,
                                                                   String orderBy) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoDriverStat> list = netcarBaseInfoDriverStatMapper.selectDriverStatList(companyId, keyword);
        //企业名称添加
        list.forEach(item -> {
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<NetcarBaseInfoDriverStat> selectDriverStatByLicenseId(String companyId, String licenseId, Integer pageNo, Integer pageSize, String orderBy) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoDriverStat> list = netcarBaseInfoDriverStatMapper.selectDriverStatByLicenseId(companyId, licenseId);
        //企业名称添加
        list.forEach(item -> {
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }
}
