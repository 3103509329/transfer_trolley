package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompany;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;
@Mapper
public interface NetcarBaseInfoCompanyMapper {


    int deleteByPrimaryKey(@Param("companyId") String companyId, @Param("address") Integer address);

    int insert(NetcarBaseInfoCompany record);

    int insertSelective(NetcarBaseInfoCompany record);

    NetcarBaseInfoCompany selectByPrimaryKey(@Param("companyId") String companyId, @Param("address") Integer address);

    int updateByPrimaryKeySelective(NetcarBaseInfoCompany record);

    int updateByPrimaryKey(NetcarBaseInfoCompany record);

    List<NetcarBaseInfoCompany> selectCompanyList(@Param("companyId") String companyId);

    NetcarBaseInfoCompany selectByCompanyId(String companyId);

    List<Map<String, Object>> getCompanyByAddress(Integer address);

    List<NetcarBaseInfoCompany> queryCompanyListByIds(@Param("corpIds") Set<String> corpIds);

}