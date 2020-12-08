package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompanyFare;
import com.zhcx.netcar.facade.base.CompanyFareService;
import com.zhcx.netcar.facade.base.CompanyServiceService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarBaseInfoCompanyFareMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 13:59
 */
@Service
public class CompanyFareServiceImpl implements CompanyFareService {

    @Autowired
    private NetcarBaseInfoCompanyFareMapper netcarBaseInfoCompanyFareMapper;

    @Autowired
    private CompanyServiceService companyServiceService;
    /**
     * 平台公司运价信息获取
     *
     * @param
     * @return
     */

    @Override
    public PageInfo<NetcarBaseInfoCompanyFare> selectCompanyFareList(String companyId, Integer pageNo, Integer pageSize, String orderBy) throws Exception {

        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoCompanyFare> list = netcarBaseInfoCompanyFareMapper.selectCompanyFareListByCompanyId(companyId);
        list.forEach(item -> {
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }

}
