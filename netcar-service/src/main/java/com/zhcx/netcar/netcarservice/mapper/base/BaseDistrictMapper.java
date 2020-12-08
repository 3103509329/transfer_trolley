package com.zhcx.netcar.netcarservice.mapper.base;


import com.zhcx.netcar.pojo.base.BaseDistrict;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BaseDistrictMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(BaseDistrict record);

    int insertSelective(BaseDistrict record);

    BaseDistrict selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(BaseDistrict record);

    int updateByPrimaryKey(BaseDistrict record);

    List<BaseDistrict> selectListByCityId(Long cityId);

    List<BaseDistrict> selectArea();

    BaseDistrict getDistrictByAddress(Integer address);
}