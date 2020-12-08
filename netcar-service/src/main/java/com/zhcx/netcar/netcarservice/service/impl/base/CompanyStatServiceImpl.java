package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompanyStat;
import com.zhcx.netcar.facade.base.CompanyStatService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarBaseInfoCompanyStatMapper;
import com.zhcx.netcar.netcarservice.utils.CompanyUtils;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 14:01
 */
@Service
public class CompanyStatServiceImpl implements CompanyStatService {

    @Autowired
    private NetcarBaseInfoCompanyStatMapper netcarBaseInfoCompanyStatMapper;

    @Autowired
    private CompanyUtils companyUtils;

    /**
     * 获取平台公司营运规模信息
     *
     * @param
     * @return
     */
    @Override
    public PageInfo<NetcarBaseInfoCompanyStat> selectCompanyStatList(String companyId, Integer pageNo, Integer pageSize, String orderBy) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoCompanyStat> netcarBaseInfoCompanyStatList = netcarBaseInfoCompanyStatMapper.selectCompanyId(companyId);
        //添加企业名称
        if (netcarBaseInfoCompanyStatList.size() != 0){
            netcarBaseInfoCompanyStatList = companyUtils.addCompanyName(netcarBaseInfoCompanyStatList);
        }
        return new PageInfo<>(netcarBaseInfoCompanyStatList);
    }

    @Override
    public List<Map<String, Object>> getCountByYear(String date, String companyId, String corpId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", date.substring(0, 4));
        params.put("companyId", companyId);
        params.put("corpId", corpId);
        return netcarBaseInfoCompanyStatMapper.getCountByYear(params);
    }

    @Override
    public List<Map<String, Object>> getCarSum(String corpId, String startTime, String type, String companyId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("companyId", companyId);
        params.put("corpId", corpId);
        params.put("startTime", startTime);
        if (type.equals("month")) {
            return netcarBaseInfoCompanyStatMapper.getCarSumByMonth(params);
        } else {
            return netcarBaseInfoCompanyStatMapper.getCarSumByYear(params);
        }
    }

    @Override
    public List<Map<String, Object>> getDriverSum(String corpId, String startTime, String type, String companyId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("companyId", companyId);
        params.put("corpId", corpId);
        params.put("startTime", startTime);
        if (type.equals("month")) {
            return netcarBaseInfoCompanyStatMapper.getDriverSumByMonth(params);
        } else {
            return netcarBaseInfoCompanyStatMapper.getDriverSumByYear(params);
        }

    }
}
