package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoDriver;
import com.zhcx.netcar.facade.base.CompanyServiceService;
import com.zhcx.netcar.facade.base.DriverService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarBaseInfoCompanyMapper;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarBaseInfoDriverMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import com.zhcx.netcar.vo.BaseInfoEmpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 14:02
 */
@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private NetcarBaseInfoDriverMapper netcarBaseInfoDriverMapper;

    @Autowired
    private CompanyServiceService companyServiceService;

    @Autowired
    private NetcarBaseInfoCompanyMapper netcarBaseInfoCompanyMapper;

    @Override
    public PageInfo<NetcarBaseInfoDriver> selectDriverList(String companyId, String type, String keyword, Integer pageNo, Integer pageSize, String orderBy) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoDriver> list = netcarBaseInfoDriverMapper.selectDriverList(companyId, type, keyword);
        list.forEach(item -> {
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

    @Override
    public NetcarBaseInfoDriver selectDriverInfoByCompanyId(String companyId, String licenseId) throws Exception {
        NetcarBaseInfoDriver netcarBaseInfoDriver = netcarBaseInfoDriverMapper.selectDriverInfoByCompanyId(companyId, licenseId);
        //企业名称添加
        if (null != netcarBaseInfoDriver) {
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(netcarBaseInfoDriver.getCompanyId(), netcarBaseInfoDriver.getAddress());
            netcarBaseInfoDriver.setCompanyName(companyName);
        }
        return netcarBaseInfoDriver;
    }

    @Override
    public List queryLicenseIdListByCompanyId(String companyId) {
        return netcarBaseInfoDriverMapper.queryLicenseIdListByCompanyId(companyId);
    }

    @Override
    public Integer selectAddressByCompanyIdAndLicenseId(String companyId, String licenseId) {
        return netcarBaseInfoDriverMapper.selectAddressByCompanyIdAndLicenseId(companyId, licenseId);
    }

//    @Override
//    public PageInfo<NetcarBaseInfoDriver> selectIllegalDriverList(String companyId, String type, String keyword, Integer pageNo, Integer pageSize, String orderBy) {
//        PageHelper.startPage(pageNo, pageSize);
//        PageHelperUtil.orderBy(orderBy);
//        List<NetcarBaseInfoDriver> list = netcarBaseInfoDriverMapper.selectIllegalDriverList(companyId, type, keyword);
//        list.forEach(item -> {
//            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
//            item.setCompanyName(companyName);
//        });
//        return new PageInfo<>(list);
//    }

//    @Override
//    public PageInfo<DriverExcel> selectDriverExcelList(String companyId, String type, String keyword, Integer pageNo, Integer pageSize, String orderBy) throws Exception {
//        PageHelper.startPage(pageNo, pageSize);
//        PageHelperUtil.orderBy(orderBy);
//        List<DriverExcel> list = netcarBaseInfoDriverMapper.selectDriverExcelList(companyId, type, keyword);
//        list.forEach(item -> {
//            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
//            item.setCompanyName(companyName);
//        });
//        return new PageInfo<>(list);
//    }

    /**
     * 获取违规驾驶员（工作台）
     *
     * @param keyword
     * @param companyId
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
//    @Override
//    public PageInfo<NetcarBaseInfoDriver> getDriverIllegal(String keyword, String type, String companyId, Integer pageNo, Integer pageSize, String orderBy) {
//        PageHelper.startPage(pageNo, pageSize);
//        PageHelperUtil.orderBy(orderBy);
//        List<NetcarBaseInfoDriver> netcarBaseInfoDrivers = netcarBaseInfoDriverMapper.getDriverIllegal(keyword, type, companyId);
//        netcarBaseInfoDrivers.forEach(item -> {
//            String companyName = netcarBaseInfoCompanyMapper.selectByCompanyId(item.getCompanyId()).getCompanyName();
//            item.setCompanyName(companyName);
//        });
//        return new PageInfo<>(netcarBaseInfoDrivers);
//    }


    @Override
    public void saveBatchEmpl(List<BaseInfoEmpl> insertEmplList) {
        netcarBaseInfoDriverMapper.saveBatchEmpl(insertEmplList);
    }

    @Override
    public void updateBatchEmpl(List<BaseInfoEmpl> updateEmplList) {
        netcarBaseInfoDriverMapper.updateBatchEmpl(updateEmplList);
    }
}
