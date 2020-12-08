package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompanyService;
import com.zhcx.netcar.constant.CompanyDataTypeEnum;
import com.zhcx.netcar.facade.base.CompanyServiceService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarBaseInfoCompanyServiceMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import com.zhcx.netcar.vo.CompanyNameVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author liliang
 * @email 3103509329@qq.com
 * @date 2018-11-24 14:00
 */
@Service
public class CompanyServiceServiceImpl implements CompanyServiceService {

    @Resource(name = "redisTemplate4Json")
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private NetcarBaseInfoCompanyServiceMapper netcarBaseInfoCompanyServiceMapper;

    @Override
    public PageInfo<NetcarBaseInfoCompanyService> selectCompanyServiceList(String companyId, Integer pageNo, Integer pageSize, String orderBy) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoCompanyService> list = netcarBaseInfoCompanyServiceMapper.selectListByCompanyId(companyId);
        return new PageInfo<>(list);

    }

    /**
     * 根据公司标识和行政代码 前4位区分所属地市
     * @param companyId
     * @param address
     * @return
     */
    @Override
    public String selectCompanyNameByCompanyIdAndAddress(String companyId, Integer address) {
        String addStr = String.valueOf(address);
        if (StringUtils.isNotBlank(addStr) && addStr.length() > 4) {
            addStr = StringUtils.substring(addStr, 0, 4);
            address = Integer.parseInt(addStr);
        }
        String key = CompanyDataTypeEnum.NETCAR_COMPANY.getDesc() + companyId + "_" + address;
        String companyName = redisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(companyName)) {
            companyName = netcarBaseInfoCompanyServiceMapper.selectCompanyNameByCompanyIdAndAddress(companyId, address);
            redisTemplate.opsForValue().set(key, companyName, 12, TimeUnit.HOURS);
        }
        return companyName;
    }

    @Override
    public List<String> selectAllCompanyIdList() {
        return netcarBaseInfoCompanyServiceMapper.selectAllCompanyIdList();
    }

    /**
     * 基于运政企业数据查询部级企业数据
     * @param uuid
     * @return
     */
    @Override
    public String selectCompanyIdByYZuuid(Long uuid) throws Exception{

        return netcarBaseInfoCompanyServiceMapper.selectCompanyIdByYZuuid(uuid);
    }

    /**
     * 获取指定公司的名称
     * @param key
     * @return
     */
    @Override
    public String selectCompanyNameByCompanyId(String key) {

        return netcarBaseInfoCompanyServiceMapper.selectCompanyNameByCompanyId(key);
    }

    @Override
    public NetcarBaseInfoCompanyService selectByCompanyId(String companyId){
        //添加企业名称
        return netcarBaseInfoCompanyServiceMapper.selectOneByCompanyId(companyId);
    }

    @Override
    public List<NetcarBaseInfoCompanyService> queryCompanyListByIds(Set<String> corpIds) {
        return netcarBaseInfoCompanyServiceMapper.queryCompanyListByIds(corpIds);
    }

    @Override
    public PageInfo<NetcarBaseInfoCompanyService> selectIllegalCompanyServiceList(String companyId, Integer pageNo, Integer pageSize, String orderBy) {
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarBaseInfoCompanyService> list = netcarBaseInfoCompanyServiceMapper.selectIllegalListByCompanyId(companyId);
        return new PageInfo<>(list);
    }

    @Override
    public List<CompanyNameVo> selectCompanyIdAndName(String companyId, String keyword) {
        return netcarBaseInfoCompanyServiceMapper.selectCompanyServiceNameByCompanyId(companyId,keyword);
    }

    @Override
    public String selectCompanyNameByRegionId(Long regionId) {
        return netcarBaseInfoCompanyServiceMapper.selectCompanyNameByRegionId(regionId);
    }
}
