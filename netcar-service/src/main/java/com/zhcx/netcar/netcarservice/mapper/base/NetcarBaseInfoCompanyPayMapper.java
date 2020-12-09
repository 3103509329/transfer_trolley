package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompanyPay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface NetcarBaseInfoCompanyPayMapper {

    int deleteByPrimaryKey(@Param("companyId") String companyId);

    int insert(NetcarBaseInfoCompanyPay record);

    int insertSelective(NetcarBaseInfoCompanyPay record);

    NetcarBaseInfoCompanyPay selectByPrimaryKey(@Param("companyId") String companyId);

    int updateByPrimaryKeySelective(NetcarBaseInfoCompanyPay record);

    int updateByPrimaryKey(NetcarBaseInfoCompanyPay record);

    List<NetcarBaseInfoCompanyPay> selectAll();

    List<NetcarBaseInfoCompanyPay> selectListByCompanyId(@Param("companyId") String companyId);

}