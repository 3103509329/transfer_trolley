package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.zhcx.netcar.pojo.base.NetcarShareCompany;
import com.zhcx.netcar.constant.CompanyDataTypeEnum;
import com.zhcx.netcar.facade.base.ShareCompanyService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarShareCompanyMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import com.zhcx.netcar.vo.CompanyNameVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/1/7 16:29
 **/
@Service("shareCompanyService")
public class ShareCompanyServiceImpl implements ShareCompanyService {
    @Resource(name = "redisTemplate4Json")
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private NetcarShareCompanyMapper netcarShareCompanyMapper;

    @Override
    public PageInfo<NetcarShareCompany> queryShareCompanyList(String companyId, String identifier, Integer pageNo, Integer pageSize, String orderBy) {
        PageHelper.startPage(pageNo, pageSize);
        PageHelperUtil.orderBy(orderBy);
        List<NetcarShareCompany> list =  netcarShareCompanyMapper.queryShareCompanyList(companyId, identifier);
        return new PageInfo<>(list);
    }

    @Override
    public Map<String, String> queryNameAndId() {
        List<NetcarShareCompany> list =  netcarShareCompanyMapper.queryShareCompanyList(null, null);
        Map<String, String> map = Maps.newHashMap();
        list.forEach(item->{
            map.put(item.getCompanyId(), item.getCompanyName());
        });

        return map;
    }

    @Override
    public List<CompanyNameVo> queryShareCompanyNameList(String companyId) {
        return netcarShareCompanyMapper.queryShareCompanyNameList(companyId);
    }

    @Override
    public String selectCompanyNameByCompanyIdAndAddress(String companyId, Integer address) {

        String companyName = redisTemplate.opsForValue().get(CompanyDataTypeEnum.NETCAR_COMPANY.getDesc() + companyId);
        if (StringUtils.isBlank(companyName)) {
            companyName = netcarShareCompanyMapper.selectCompanyNameByCompanyIdAndAddress(companyId, address);
        }
        return companyName;
    }
}
