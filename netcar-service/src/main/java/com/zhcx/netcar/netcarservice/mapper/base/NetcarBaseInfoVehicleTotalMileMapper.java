package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarBaseInfoVehicleTotalMile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface NetcarBaseInfoVehicleTotalMileMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarBaseInfoVehicleTotalMile record);

    NetcarBaseInfoVehicleTotalMile selectByPrimaryKey(Long uuid);

    List<NetcarBaseInfoVehicleTotalMile> selectAll();

    int updateByPrimaryKey(NetcarBaseInfoVehicleTotalMile record);

    int updateByPrimaryKeySelective(NetcarBaseInfoVehicleTotalMile record);

    List<NetcarBaseInfoVehicleTotalMile> selectVehicleTotalMileList(@Param("companyId") String companyId, @Param("vehicleNo") String vehicleNo);

    List<NetcarBaseInfoVehicleTotalMile> selectVehicleTotalMileByVehicleNo(@Param("companyId") String companyId, @Param("vehicleNo") String vehicleNo);
}