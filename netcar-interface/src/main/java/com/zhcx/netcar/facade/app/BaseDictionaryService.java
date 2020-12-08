package com.zhcx.netcar.facade.app;

import com.zhcx.netcar.pojo.base.BaseDictionary;
import com.zhcx.netcar.pojo.base.BaseDistrict;

import java.util.List;
import java.util.Map;

public interface BaseDictionaryService {

    List<BaseDictionary> selectDictionary(String categoryCode);

    List districtList(String type, String parent);

    Long getCityId(String cityName) throws Exception;

    BaseDistrict getDistrictById(String districtId) throws Exception;

    Map<String,String> selectDictionaryByCode(String jjlx);
}
