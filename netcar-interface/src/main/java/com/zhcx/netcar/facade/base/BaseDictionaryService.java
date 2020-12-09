package com.zhcx.netcar.facade.base;


import com.zhcx.netcar.pojo.base.BaseDictionary;
import com.zhcx.netcar.pojo.base.BaseDistrict;

import java.util.List;

public interface BaseDictionaryService {

    List<BaseDictionary> selectDictionary(String categoryCode);

    /**
     * 根据类型（省/市/区）和关键字（父级ID）：查询对应省/市/区下面的所有信息
     * @param type
     * @param parent
     * @return
     */
    List districtList(String type, String parent);

    /**
     * 根据城市名称查询 城市主键
     * @param cityName
     * @return
     * @throws Exception
     */
    Long getCityId(String cityName) throws Exception;

    /**
     * 根据区表主键 查询县级（区）信息
     * @param districtId
     * @return
     * @throws Exception
     */
    BaseDistrict getDistrictById(String districtId) throws Exception;

    /**
     * 根据行政区域代号 获取区域信息
     * @param address
     * @return
     */
    BaseDistrict getDistrictByAddress(Integer address);
}
