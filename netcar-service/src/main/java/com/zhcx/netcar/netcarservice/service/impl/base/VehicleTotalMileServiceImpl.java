package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoVehicleTotalMile;
import com.zhcx.netcar.facade.base.CompanyServiceService;
import com.zhcx.netcar.facade.base.VehicleTotalMileService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarBaseInfoVehicleTotalMileMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 14:08
 */
@Service
public class VehicleTotalMileServiceImpl implements VehicleTotalMileService {

    @Autowired
    private NetcarBaseInfoVehicleTotalMileMapper netcarBaseInfoVehicleTotalMileMapper;

    @Autowired
    private CompanyServiceService companyServiceService;

    @Override
    public PageInfo<NetcarBaseInfoVehicleTotalMile> selectVehicleTotalMileList(String companyId,
                                                                               String vehicleNo,
                                                                               Integer pageNo,
                                                                               Integer pageSize,
                                                                               String orderBy) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoVehicleTotalMile> list = netcarBaseInfoVehicleTotalMileMapper.selectVehicleTotalMileList(companyId, vehicleNo);
        //企业名称添加
        list.forEach(item -> {
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<NetcarBaseInfoVehicleTotalMile> selectVehicleTotalMileByVehicleNo(String companyId, String vehicleNo, Integer pageNo, Integer pageSize, String orderBy) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoVehicleTotalMile> list = netcarBaseInfoVehicleTotalMileMapper.selectVehicleTotalMileByVehicleNo(companyId, vehicleNo);
        //添加企业名称
        list.forEach(item -> {
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }
}
