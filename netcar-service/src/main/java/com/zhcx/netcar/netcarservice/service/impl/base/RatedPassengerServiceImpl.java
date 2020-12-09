package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.base.NetcarOnOffLineService;
import com.zhcx.netcar.pojo.base.NetcarOperateLogin;
import com.zhcx.netcar.pojo.base.NetcarRatedPassenger;
import com.zhcx.netcar.facade.base.CompanyServiceService;
import com.zhcx.netcar.facade.base.OrderService;
import com.zhcx.netcar.facade.base.RatedPassengerService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarRatedPassengerMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import com.zhcx.netcar.params.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/11/27 16:32
 **/
@Service("ratedPassengerService")
public class RatedPassengerServiceImpl implements RatedPassengerService {

    @Autowired
    private NetcarRatedPassengerMapper netcarRatedPassengerMapper;

    @Autowired
    private CompanyServiceService companyServiceService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private NetcarOnOffLineService netcarOnOffLineService;

    @Override
    public PageInfo<NetcarRatedPassenger> queryPassengerRatedList(QueryParam param) throws Exception {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarRatedPassenger> list = netcarRatedPassengerMapper.selectPassengerRatedListByCondition(param);
        //添加企业名称
        list.forEach(item -> {
            Integer address = orderService.selectAddressByCompanyIdAndOrderId(item.getCompanyId(), item.getOrderId());
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), address);
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

    /**
     * 查询乘客评价信息（基于车牌）
     *
     * @param param
     * @return
     */
    @Override
    public PageInfo<NetcarRatedPassenger> queryPassengerRatedVehicleNo(QueryParam param) {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarRatedPassenger> list = netcarRatedPassengerMapper.queryPassengerRatedVehicleNo(param);
        //添加企业名称
        list.forEach(item -> {
            Integer address = orderService.selectAddressByCompanyIdAndOrderId(item.getCompanyId(), item.getOrderId());
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), address);
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

}
