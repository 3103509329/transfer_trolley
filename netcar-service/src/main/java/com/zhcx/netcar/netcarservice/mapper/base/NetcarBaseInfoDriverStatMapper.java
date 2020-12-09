package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarBaseInfoDriverStat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface NetcarBaseInfoDriverStatMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarBaseInfoDriverStat record);

    NetcarBaseInfoDriverStat selectByPrimaryKey(Long uuid);

    List<NetcarBaseInfoDriverStat> selectAll();

    int updateByPrimaryKey(NetcarBaseInfoDriverStat record);

    List<NetcarBaseInfoDriverStat> selectDriverStatList(@Param("companyId") String companyId, @Param("keyword") String keyword);

    int updateByPrimaryKeySelective(NetcarBaseInfoDriverStat record);


    List<NetcarBaseInfoDriverStat> selectDriverStatByLicenseId(@Param("companyId") String companyId, @Param("licenseId") String licenseId);
}