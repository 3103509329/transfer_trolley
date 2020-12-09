package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompanyFare;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface NetcarBaseInfoCompanyFareMapper {

    List<NetcarBaseInfoCompanyFare> selectCompanyFareListByCompanyId(@Param("companyId")String companyId);

    int deleteByPrimaryKey(@Param("companyId") String companyId, @Param("address") Integer address);

    int insert(NetcarBaseInfoCompanyFare record);

    int insertSelective(NetcarBaseInfoCompanyFare record);

    NetcarBaseInfoCompanyFare selectByPrimaryKey(@Param("companyId") String companyId, @Param("address") Integer address);

    int updateByPrimaryKeySelective(NetcarBaseInfoCompanyFare record);

    int updateByPrimaryKey(NetcarBaseInfoCompanyFare record);
}