package com.zhcx.netcar.netcarservice.mapper.base;


import com.zhcx.netcar.pojo.base.NetcarBaseInfoPassenger;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface NetcarBaseInfoPassengerMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarBaseInfoPassenger record);

    int insertSelective(NetcarBaseInfoPassenger record);

    NetcarBaseInfoPassenger selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarBaseInfoPassenger record);

    int updateByPrimaryKey(NetcarBaseInfoPassenger record);

    List<NetcarBaseInfoPassenger> selectListByCompanyAndPhone(@Param("companyId") String companyId, @Param("passengerPhone") String passengerPhone);
}