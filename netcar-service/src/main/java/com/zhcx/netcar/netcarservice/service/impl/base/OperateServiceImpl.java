package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.base.CompanyServiceService;
import com.zhcx.netcar.facade.base.OperateService;
import com.zhcx.netcar.facade.base.OrderService;
import com.zhcx.netcar.facade.base.VehicleService;
import com.zhcx.netcar.netcarservice.mapper.base.*;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import com.zhcx.netcar.params.OperateParam;
import com.zhcx.netcar.pojo.base.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/10 21:29
 **/
@Service("operateService")
public class OperateServiceImpl implements OperateService {


    @Autowired
    private NetcarOperateInfoMapper netcarOperateInfoMapper;

    @Autowired
    private NetcarOperateLoginMapper netcarOperateLoginMapper;

    @Autowired
    private NetcarOperateLogoutMapper netcarOperateLogoutMapper;

    @Autowired
    private NetcarOperateDepartMapper netcarOperateDepartMapper;

    @Autowired
    private NetcarOperateArriveMapper netcarOperateArriveMapper;

    @Autowired
    private NetcarOperatePayMapper netcarOperatePayMapper;

    @Autowired
    private CompanyServiceService companyServiceService;

//    @Autowired
//    private DriverService driverService;
    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private OrderService orderService;

    @Override
    public PageInfo<NetcarOperateLogin> queryOperateLoginList(OperateParam param) throws Exception {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarOperateLogin> list = netcarOperateLoginMapper.queryOperateLoginListByCondition(param);

        //添加企业名称
        list.forEach(item -> {
//            Integer address = driverService.selectAddressByCompanyIdAndLicenseId(item.getCompanyId(), item.getLicenseId());
            Integer address = vehicleService.selectAddressByCompanyIdAndVehicleNo(item.getCompanyId(), item.getVehicleNo());

            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), address);
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<NetcarOperateLogout> queryOperateLogoutList(OperateParam param) throws Exception {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarOperateLogout> list = netcarOperateLogoutMapper.queryOperateLogoutListByCondition(param);

        //添加企业名称
        list.forEach(item -> {
//            Integer address = driverService.selectAddressByCompanyIdAndLicenseId(item.getCompanyId(), item.getVehicleNo());
            Integer address = vehicleService.selectAddressByCompanyIdAndVehicleNo(item.getCompanyId(), item.getVehicleNo());
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), address);
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<NetcarOperateDepart> queryOperateDepartList(OperateParam param) throws Exception {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarOperateDepart> list = netcarOperateDepartMapper.queryOperateDepartListByCondition(param);

        //添加企业名称
        list.forEach(item -> {
            Integer address = orderService.selectAddressByCompanyIdAndOrderId(item.getCompanyId(), item.getOrderId());
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), address);
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<NetcarOperateArrive> queryOperateArriveList(OperateParam param) throws Exception {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarOperateArrive> list = netcarOperateArriveMapper.queryOperateArriveListByCondition(param);

        //添加企业名称
        list.forEach(item -> {
            Integer address = orderService.selectAddressByCompanyIdAndOrderId(item.getCompanyId(), item.getOrderId());
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), address);
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<NetcarOperatePay> queryOperatePayList(OperateParam param) throws Exception {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarOperatePay> list = netcarOperatePayMapper.queryOperatePayListByCondition(param);

        //添加企业名称
        list.forEach(item -> {
            Integer address = orderService.selectAddressByCompanyIdAndOrderId(item.getCompanyId(), item.getOrderId());
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), address);
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<NetcarOperateInfo> queryOperateInfoList(OperateParam param) throws Exception {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarOperateInfo> list = netcarOperateInfoMapper.queryOperateInfoListByCondition(param);

        //添加企业名称
        list.forEach(item -> {
            Integer address = orderService.selectAddressByCompanyIdAndOrderId(item.getCompanyId(), item.getOrderId());
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), address);
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }
}
