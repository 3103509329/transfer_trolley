package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarShareInfo;
import com.zhcx.netcar.pojo.base.NetcarShareOrder;
import com.zhcx.netcar.pojo.base.NetcarSharePay;
import com.zhcx.netcar.pojo.base.NetcarShareRoute;
import com.zhcx.netcar.facade.base.ShareCompanyService;
import com.zhcx.netcar.facade.base.ShareService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarShareInfoMapper;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarShareOrderMapper;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarSharePayMapper;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarShareRouteMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import com.zhcx.netcar.params.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2018/11/29 0029 14:25
 **/
@Service("shareService")
public class ShareServiceImpl implements ShareService {

    @Autowired
    private NetcarShareRouteMapper netcarShareRouteMapper;

    @Autowired
    private NetcarShareOrderMapper netcarShareOrderMapper;

    @Autowired
    private NetcarSharePayMapper netcarSharePayMapper;

    @Autowired
    private ShareCompanyService shareCompanyService;

    @Autowired
    private NetcarShareInfoMapper netcarShareInfoMapper;

    @Override
    public PageInfo<NetcarShareRoute> queryShareRouteList(QueryParam param) throws Exception {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarShareRoute> list = netcarShareRouteMapper.queryShareRouteListByCondition(param);

        list.forEach(item -> {
            String companyName = shareCompanyService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });

        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<NetcarShareOrder> queryShareOrderList(QueryParam param) throws Exception {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarShareOrder> list = netcarShareOrderMapper.queryShareOrderListByCondition(param);
        //添加企业名称
        list.forEach(item -> {
            String companyName = shareCompanyService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });

        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<NetcarSharePay> querySharePayList(QueryParam param) throws Exception {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarSharePay> list = netcarSharePayMapper.querySharePayListByCondition(param);
        //添加企业名称
        list.forEach(item -> {
            String companyName = shareCompanyService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<NetcarShareInfo> queryShareInfoList(QueryParam param) throws Exception {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarShareInfo> list = netcarShareInfoMapper.queryShareInfoListByCondition(param);
        //添加企业名称
        list.forEach(item -> {
            String companyName = shareCompanyService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

}
