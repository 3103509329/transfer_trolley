package com.zhcx.netcar.netcarservice.service.impl.app;

import com.google.common.collect.Maps;
import com.zhcx.netcar.facade.app.BaseDictionaryService;
import com.zhcx.netcar.netcarservice.mapper.base.BaseDictionaryMapper;
import com.zhcx.netcar.netcarservice.mapper.base.BaseDistrictMapper;
import com.zhcx.netcar.pojo.base.BaseDictionary;
import com.zhcx.netcar.pojo.base.BaseDistrict;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class BaseDictionaryServiceImpl implements BaseDictionaryService {

    private Logger log = LoggerFactory.getLogger(BaseDictionaryServiceImpl.class);

    @Resource
    private BaseDictionaryMapper dictionaryMapper;

    @Resource
    private BaseDistrictMapper districtMapper;

    @Override
    public List<BaseDictionary> selectDictionary(String categoryCode) {
        return dictionaryMapper.selectDictionary(categoryCode);
    }

    @Override
    public List districtList(String type, String parent) {
        List list = null;
        //省列表
        if("P".equals(type) || StringUtils.isBlank(type)){
            list = dictionaryMapper.provinceList();
        }
        //市列表
        if("C".equals(type)){
            list = dictionaryMapper.cityList(parent);
        }
        //区列表
        if("D".equals(type)){
            list = dictionaryMapper.districtList(parent);
        }
        return list;
    }

    @Override
    public Long getCityId(String cityName) throws Exception {
        return dictionaryMapper.getCityId(cityName);
    }

    @Override
    public BaseDistrict getDistrictById(String districtId) throws Exception {
        return districtMapper.selectByPrimaryKey(Long.parseLong(districtId));
    }

    @Override
    public Map<String, String> selectDictionaryByCode(String categoryCode) {
        List<BaseDictionary> baseDictionaryList = dictionaryMapper.selectDictionary(categoryCode);
        Map<String, String> resultMap = Maps.newHashMap();
        baseDictionaryList.forEach(baseDictionary -> {
            resultMap.put(baseDictionary.getItemCode(), baseDictionary.getItem());
        });
        return resultMap;
    }

}
