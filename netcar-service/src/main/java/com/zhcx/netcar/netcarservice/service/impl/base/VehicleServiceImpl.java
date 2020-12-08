package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompany;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoVehicle;
import com.zhcx.netcar.facade.base.CompanyServiceService;
import com.zhcx.netcar.facade.base.VehicleService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarBaseInfoCompanyMapper;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarBaseInfoVehicleMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import com.zhcx.netcar.vo.BaseInfoVehiclelicence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 14:07
 */
@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private NetcarBaseInfoVehicleMapper netcarBaseInfoVehicleMapper;

    @Autowired
    private CompanyServiceService companyServiceService;

    @Autowired
    private NetcarBaseInfoCompanyMapper netcarBaseInfoCompanyMapper;


    @Override
    public PageInfo<NetcarBaseInfoVehicle> selectVehicleList(String companyId, String vehicleNo, Integer pageNo, Integer pageSize, String orderBy) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoVehicle> list = netcarBaseInfoVehicleMapper.selectVehicleBaseList(companyId, vehicleNo);
        //添加企业名称
        list.forEach(item -> {
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

    @Override
    public NetcarBaseInfoVehicle selectVehicleInfo(String companyId, String vehicleNo) throws Exception {
        NetcarBaseInfoVehicle netcarBaseInfoVehicle = netcarBaseInfoVehicleMapper.selectByCompanyIdAndVehicleNo(companyId, vehicleNo);
        //添加企业名称
        if (null != netcarBaseInfoVehicle) {
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(netcarBaseInfoVehicle.getCompanyId(), netcarBaseInfoVehicle.getAddress());
            netcarBaseInfoVehicle.setCompanyName(companyName);
        }
        return netcarBaseInfoVehicle;
    }

    @Override
    public List<NetcarBaseInfoVehicle> queryCompanyVehicleList(List<String> corpIds) throws Exception {
        if (null == corpIds || corpIds.size() == 0) {
            return Lists.newArrayList();
        }
        return netcarBaseInfoVehicleMapper.queryCompanyVehicleList(corpIds);
    }

    @Override
    public List<String> selectVehicleNoListByCompanyId(String companyId) {

        return netcarBaseInfoVehicleMapper.selectVehicleNoListByCompanyId(companyId);
    }

    @Override
    public List<NetcarBaseInfoVehicle> queryVehicleListByAddress(String address) {
        return netcarBaseInfoVehicleMapper.queryVehicleListByAddress(address);
    }

    @Override
    public Integer selectAddressByCompanyIdAndVehicleNo(String companyId, String vehicleNo) {
        return netcarBaseInfoVehicleMapper.selectAddressByCompanyIdAndVehicleNo(companyId, vehicleNo);
    }

    @Override
    public PageInfo<NetcarBaseInfoVehicle> selectIllegalVehicleList(String companyId, String vehicleNo, Integer pageNo, Integer pageSize, String orderBy) {
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoVehicle> list = netcarBaseInfoVehicleMapper.selectIllegalVehicleList(companyId, vehicleNo);
        //添加企业名称
        list.forEach(item -> {
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

    /**
     * 获取违规车辆
     *
     * @param companyId
     * @param vehicleNo
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    @Override
    public PageInfo<NetcarBaseInfoVehicle> getVehilceIllegal(String companyId, String vehicleNo, Integer pageNo, Integer pageSize, String orderBy) {
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoVehicle> netcarBaseInfoVehicleList = netcarBaseInfoVehicleMapper.getVehilceIllegal(companyId, vehicleNo);
        netcarBaseInfoVehicleList.forEach(item -> {
            String companyName = netcarBaseInfoCompanyMapper.selectByCompanyId(item.getCompanyId()).getCompanyName();
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(netcarBaseInfoVehicleList);
    }

    /**
     * 查询违规违规企业List或指定违规企业
     *
     * @param companyId
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    @Override
    public PageInfo<NetcarBaseInfoCompany> getCompanyList(String companyId, Integer pageNo, Integer pageSize, String orderBy) {
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoCompany> companyList = new ArrayList<>();
        NetcarBaseInfoCompany company = new NetcarBaseInfoCompany();
        if (null == companyId) {
            companyList = netcarBaseInfoVehicleMapper.getCompanyList();
        } else {
            company = netcarBaseInfoCompanyMapper.selectByCompanyId(companyId);
            if (null != company) {
                companyList.add(company);
            }
        }
        return new PageInfo<>(companyList);
    }

    /**
     * 获取违规企业列表（所有企业）
     *
     * @return
     */
    @Override
    public PageInfo<NetcarBaseInfoCompany> getCompanys() {
        List<NetcarBaseInfoCompany> companyList = netcarBaseInfoVehicleMapper.getCompanyList();
        List<NetcarBaseInfoCompany> result = new ArrayList<>();
        companyList.forEach(company ->
        {
            NetcarBaseInfoCompany data = new NetcarBaseInfoCompany();
            data.setCompanyId(company.getCompanyId());
            data.setCompanyName(company.getCompanyName());
            result.add(data);
        });
        return new PageInfo<>(result);
    }

    @Override
    public void insertBatchBaseVehicle(List<BaseInfoVehiclelicence> insertBaseVehicleList) {
        netcarBaseInfoVehicleMapper.saveBaseVehicleBatch(insertBaseVehicleList);
    }

    @Override
    public void updateBatchBaseVehicle(List<BaseInfoVehiclelicence> updateBaseVehicelList) {
        netcarBaseInfoVehicleMapper.updateBatchBaseVehicle(updateBaseVehicelList);
    }
}
