package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompanyPermit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NetcarBaseInfoCompanyPermitMapper {

    List<NetcarBaseInfoCompanyPermit> selectAll();

    List<NetcarBaseInfoCompanyPermit> selectListByCompanyId(@Param("companyId") String companyId);

    int deleteByPrimaryKey(@Param("companyId") String companyId, @Param("address") Integer address);

    int insert(NetcarBaseInfoCompanyPermit record);

    int insertSelective(NetcarBaseInfoCompanyPermit record);

    NetcarBaseInfoCompanyPermit selectByPrimaryKey(@Param("companyId") String companyId, @Param("address") Integer address);

    int updateByPrimaryKeySelective(NetcarBaseInfoCompanyPermit record);

    int updateByPrimaryKey(NetcarBaseInfoCompanyPermit record);
}