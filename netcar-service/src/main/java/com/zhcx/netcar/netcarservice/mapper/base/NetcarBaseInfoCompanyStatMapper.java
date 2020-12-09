package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompanyStat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
@Mapper
public interface NetcarBaseInfoCompanyStatMapper {

    List<NetcarBaseInfoCompanyStat> selectAll();

    List<NetcarBaseInfoCompanyStat> selectCompanyId(@Param("companyId") String companyId);

    List<Map<String,Object>> getCountByYear(Map<String, Object> params);

    List<Map<String, Object>> getDriverSum(Map<String, Object> params);

    List<Map<String, Object>> getDriverSumByMonth(Map<String, Object> params);

    List<Map<String, Object>> getDriverSumByYear(Map<String, Object> params);

    List<Map<String, Object>> getCarSumByMonth(Map<String, Object> params);

    List<Map<String, Object>> getCarSumByYear(Map<String, Object> params);

    int deleteByPrimaryKey(@Param("companyId") String companyId);

    int insert(NetcarBaseInfoCompanyStat record);

    int insertSelective(NetcarBaseInfoCompanyStat record);

    NetcarBaseInfoCompanyStat selectByPrimaryKey(@Param("companyId") String companyId);

    int updateByPrimaryKeySelective(NetcarBaseInfoCompanyStat record);

    int updateByPrimaryKey(NetcarBaseInfoCompanyStat record);
}