package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarBaseInfoVehicleInsurance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface NetcarBaseInfoVehicleInsuranceMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarBaseInfoVehicleInsurance record);

    int updateByPrimaryKey(NetcarBaseInfoVehicleInsurance record);

    NetcarBaseInfoVehicleInsurance selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarBaseInfoVehicleInsurance record);

    List<NetcarBaseInfoVehicleInsurance> selectAll();

    List<NetcarBaseInfoVehicleInsurance> selectVehicleInsuranceList(@Param("companyId") String companyId, @Param("vehicleNo") String vehicleNo);

    List<NetcarBaseInfoVehicleInsurance> selectVehicleInsuranceByVehicleNo(@Param("companyId") String companyId, @Param("vehicleNo") String vehicleNo);
}