package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarBaseInfoDriverEducate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface NetcarBaseInfoDriverEducateMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarBaseInfoDriverEducate record);

    NetcarBaseInfoDriverEducate selectByPrimaryKey(Long uuid);

    List<NetcarBaseInfoDriverEducate> selectAll();

    int updateByPrimaryKey(NetcarBaseInfoDriverEducate record);

    List<NetcarBaseInfoDriverEducate> selectDriverEducateList(@Param("companyId") String companyId, @Param("licenseId") String licenseId);

    int updateByPrimaryKeySelective(NetcarBaseInfoDriverEducate record);

    List<NetcarBaseInfoDriverEducate> selectDriverEducateByLicenseId(@Param("companyId") String companyId, @Param("licenseId") String licenseId);
}