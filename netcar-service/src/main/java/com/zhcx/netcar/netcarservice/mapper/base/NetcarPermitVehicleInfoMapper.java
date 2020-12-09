package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.app.NetcarPermitVehicleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface NetcarPermitVehicleInfoMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarPermitVehicleInfo record);

    int insertSelective(NetcarPermitVehicleInfo record);

    NetcarPermitVehicleInfo selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarPermitVehicleInfo record);

    int updateByPrimaryKey(NetcarPermitVehicleInfo record);

    List<NetcarPermitVehicleInfo> selectPermitVehicleInfoList(@Param("corpId") Long corpId, @Param("keyword") String keyword, @Param("flag") Integer flag);
}