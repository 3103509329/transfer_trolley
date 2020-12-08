package com.zhcx.netcar.netcarservice.mapper.base;


import com.zhcx.netcar.pojo.base.NetcarRatedPassenger;
import com.zhcx.netcar.params.QueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface NetcarRatedPassengerMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarRatedPassenger record);

    int insertSelective(NetcarRatedPassenger record);

    NetcarRatedPassenger selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarRatedPassenger record);

    int updateByPrimaryKey(NetcarRatedPassenger record);

    List<NetcarRatedPassenger> selectPassengerRatedListByCondition(QueryParam param);

    List<NetcarRatedPassenger> queryPassengerRatedVehicleNo(QueryParam param);
}