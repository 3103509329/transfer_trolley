package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoPassenger;
import com.zhcx.netcar.facade.base.PassengerService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarBaseInfoPassengerMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/7 16:23
 **/
@Service("passengerService")
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private NetcarBaseInfoPassengerMapper netcarBaseInfoPassengerMapper;

    @Override
    public PageInfo<NetcarBaseInfoPassenger> queryPassengerList(String companyId, String passengerPhone, Integer pageNo, Integer pageSize, String orderBy) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoPassenger> netcarBaseInfoPassengerList = netcarBaseInfoPassengerMapper.selectListByCompanyAndPhone(companyId, passengerPhone);

        return new PageInfo<>(netcarBaseInfoPassengerList);
    }
}
