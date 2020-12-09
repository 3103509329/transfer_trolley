package com.zhcx.netcar.netcarservice.mapper.base;


import com.zhcx.netcar.pojo.base.BaseDictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BaseDictionaryMapper {
    int deleteByPrimaryKey(Integer uuid);

    int insert(BaseDictionary record);

    int insertSelective(BaseDictionary record);

    BaseDictionary selectByPrimaryKey(Integer uuid);

    int updateByPrimaryKeySelective(BaseDictionary record);

    int updateByPrimaryKey(BaseDictionary record);

    List<BaseDictionary> selectDictionary(@Param("categoryCode") String categoryCode);

    List provinceList();

    List cityList(String parent);

    List districtList(String parent);

    Long getCityId(String cityName);
}