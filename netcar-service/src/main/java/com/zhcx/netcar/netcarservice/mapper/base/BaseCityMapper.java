package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.BaseCity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaseCityMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(BaseCity record);

    int insertSelective(BaseCity record);

    BaseCity selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(BaseCity record);

    int updateByPrimaryKey(BaseCity record);
}