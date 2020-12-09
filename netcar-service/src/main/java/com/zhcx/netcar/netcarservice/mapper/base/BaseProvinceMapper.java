package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.BaseProvince;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaseProvinceMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(BaseProvince record);

    int insertSelective(BaseProvince record);

    BaseProvince selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(BaseProvince record);

    int updateByPrimaryKey(BaseProvince record);
}