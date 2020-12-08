package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompanyPay;
import com.zhcx.netcar.facade.base.CompanyPayService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarBaseInfoCompanyPayMapper;
import com.zhcx.netcar.netcarservice.utils.CompanyUtils;
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
public class CompanyPayServiceImpl implements CompanyPayService {

    public static final String UUIDUTILS_KEY = "netcar_base_info_company_pay";

    @Autowired
    private NetcarBaseInfoCompanyPayMapper netcarBaseInfoCompanyPayMapper;

    @Autowired
    private CompanyUtils companyUtils;
    /**
     * 获取平台公司支付信息
     *
     * @param
     * @return
     */
    @Override
    public PageInfo<NetcarBaseInfoCompanyPay> getCompanyPayList(String companyId, Integer pageNo, Integer pageSize, String orderBy) throws Exception{

        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoCompanyPay> list = netcarBaseInfoCompanyPayMapper.selectListByCompanyId(companyId);
        //添加企业名称
        if (list.size() != 0){
            list = companyUtils.addCompanyName(list);
        }
        return new PageInfo<>(list);
    }

}
