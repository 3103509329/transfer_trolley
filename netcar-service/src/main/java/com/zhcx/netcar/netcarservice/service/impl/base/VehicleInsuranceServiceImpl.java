package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoVehicleInsurance;
import com.zhcx.netcar.facade.base.CompanyServiceService;
import com.zhcx.netcar.facade.base.VehicleInsuranceService;
import com.zhcx.netcar.facade.base.VehicleService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarBaseInfoVehicleInsuranceMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 14:07
 */
@Service
public class VehicleInsuranceServiceImpl implements VehicleInsuranceService {

    @Autowired
    private NetcarBaseInfoVehicleInsuranceMapper netcarBaseInfoVehicleInsuranceMapper;

    @Autowired
    private CompanyServiceService companyServiceService;

    @Autowired
    private VehicleService vehicleService;

    @Override
    public PageInfo<NetcarBaseInfoVehicleInsurance> selectVehicleInsuranceList(String companyId, String vehicleNo, Integer pageNo, Integer pageSize, String orderBy) throws Exception {

        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoVehicleInsurance> list = netcarBaseInfoVehicleInsuranceMapper.selectVehicleInsuranceList(companyId, vehicleNo);
        //添加企业名称
        list.forEach(item -> {
            Integer address = vehicleService.selectAddressByCompanyIdAndVehicleNo(item.getCompanyId(), item.getVehicleNo());
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), address);
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<NetcarBaseInfoVehicleInsurance> selectVehicleInsuranceByVehicleNo(String companyId, String vehicleNo, Integer pageNo, Integer pageSize, String orderBy) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoVehicleInsurance> list = netcarBaseInfoVehicleInsuranceMapper.selectVehicleInsuranceByVehicleNo(companyId, vehicleNo);
        //添加企业名称
        list.forEach(item -> {
            Integer address = vehicleService.selectAddressByCompanyIdAndVehicleNo(item.getCompanyId(), item.getVehicleNo());
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), address);
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }
}
