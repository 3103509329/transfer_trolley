package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.base.CompanyServiceService;
import com.zhcx.netcar.facade.base.OrderService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarOrderCancelMapper;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarOrderCreateMapper;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarOrderInfoMapper;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarOrderMatchMapper;
import com.zhcx.netcar.netcarservice.utils.DateTimeUtil;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import com.zhcx.netcar.params.OrderParam;
import com.zhcx.netcar.pojo.base.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/10 21:02
 **/
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private NetcarOrderInfoMapper orderInfoMapper;

    @Autowired
    private NetcarOrderCreateMapper netcarOrderCreateMapper;

    @Autowired
    private NetcarOrderMatchMapper netcarOrderMatchMapper;

    @Autowired
    private NetcarOrderCancelMapper netcarOrderCancelMapper;

    @Autowired
    private CompanyServiceService companyServiceService;

    @Override
    public PageInfo<NetcarOrderCreate> queryOrderCreateList(OrderParam param) throws Exception {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarOrderCreate> list = netcarOrderCreateMapper.queryOrderCreateListByCondition(param);

        //添加企业名称
        list.forEach(item -> {
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<NetcarOrderMatch> queryOrderMatchList(OrderParam param) throws Exception {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarOrderMatch> list = netcarOrderMatchMapper.queryOrderMatchListByCondition(param);

        //添加企业名称
        list.forEach(item -> {
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<NetcarOrderCancel> queryOrderCancelList(OrderParam param) throws Exception {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarOrderCancel> list = netcarOrderCancelMapper.queryOrderCancelListByCondition(param);

        //添加企业名称
        list.forEach(item -> {
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<NetcarOrderInfo> queryOrderInfoList(OrderParam param) throws Exception {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarOrderInfo> list = orderInfoMapper.queryOrderInfoList(param);
        //添加企业名称
        list.forEach(item -> {
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

    @Override
    public Integer selectAddressByCompanyIdAndLicenseId(String companyId, String licenseId) {
        return netcarOrderMatchMapper.selectAddressByLicenseId(companyId, licenseId);
    }

    @Override
    public Integer selectAddressByCompanyIdAndOrderId(String companyId, String orderId) {
        return netcarOrderMatchMapper.selectAddressByOrderId(companyId, orderId);
    }

    @Override
    public List<NetcarOrderCreate> queryOrderPositionListByOrderCreate(String startTime, String endTime) throws Exception{
        if (StringUtils.isBlank(startTime)) {
            startTime = DateTimeUtil.getYMDHMSFormat(DateTimeUtil.getCurrentHourAgo(new Date(), 12));
            endTime = DateTimeUtil.getYMDHMSFormat(new Date());
        }
        return netcarOrderCreateMapper.queryOrderPositionList(startTime, endTime);
    }

    @Override
    public List<BookCarLevel> queryBookCarLevelByOrderCreateAndMatch(String startTime, String endTime) {
        if (StringUtils.isBlank(startTime)) {
            startTime = DateTimeUtil.getYMDHMSFormat(DateTimeUtil.getCurrentHourAgo(new Date(), 12));
            endTime = DateTimeUtil.getYMDHMSFormat(new Date());
        }
        return orderInfoMapper.queryBookCarLevelByOrderCreateAndMatch(startTime, endTime);
    }

}
