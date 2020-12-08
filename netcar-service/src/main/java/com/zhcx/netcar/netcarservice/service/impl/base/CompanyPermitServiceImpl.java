package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompanyPermit;
import com.zhcx.netcar.facade.base.CompanyPermitService;
import com.zhcx.netcar.facade.base.CompanyServiceService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarBaseInfoCompanyPermitMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 14:00
 */
@Service
public class CompanyPermitServiceImpl implements CompanyPermitService {

    @Autowired
    private NetcarBaseInfoCompanyPermitMapper netcarBaseInfoCompanyPermitMapper;

    @Autowired
    private CompanyServiceService companyServiceService;

    /**
     * 平台经营许可信息查询
     *
     * @param
     * @return
     */
    @Override
    public PageInfo<NetcarBaseInfoCompanyPermit> getCompanyPermitList(String companyId, Integer pageNo, Integer pageSize, String orderBy) throws Exception {

        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoCompanyPermit> list = netcarBaseInfoCompanyPermitMapper.selectListByCompanyId(companyId);
        list.forEach(item -> {
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), item.getAddress());
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }
}
