package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarRatedDriverPunish;
import com.zhcx.netcar.facade.base.CompanyServiceService;
import com.zhcx.netcar.facade.base.DriverService;
import com.zhcx.netcar.facade.base.RatedDriverPunishService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarRatedDriverPunishMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import com.zhcx.netcar.params.RatedDriverParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/11/28 10:06
 **/
@Service("ratedDriverPunishService")
public class RatedDriverPunishServiceImpl implements RatedDriverPunishService {

    @Autowired
    private NetcarRatedDriverPunishMapper netcarRatedDriverPunishMapper;

    @Autowired
    private CompanyServiceService companyServiceService;

    @Autowired
    private DriverService driverService;
    @Override
    public PageInfo<NetcarRatedDriverPunish> queryDriverPunish(RatedDriverParam param) throws Exception{
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarRatedDriverPunish> list = netcarRatedDriverPunishMapper.selectListByKeyword(param);
        list.forEach(item -> {
            Integer address = driverService.selectAddressByCompanyIdAndLicenseId(item.getCompanyId(), item.getLicenseId());
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), address);
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

}
