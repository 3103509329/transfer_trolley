package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarRatedDriver;
import com.zhcx.netcar.params.RatedDriverParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface NetcarRatedDriverMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarRatedDriver record);

    int insertSelective(NetcarRatedDriver record);

    NetcarRatedDriver selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarRatedDriver record);

    int updateByPrimaryKey(NetcarRatedDriver record);

    List<NetcarRatedDriver> selectListByKeyword(RatedDriverParam param);
}