package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarBaseInfoDriverApp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
@Mapper

public interface NetcarBaseInfoDriverAppMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarBaseInfoDriverApp record);

    NetcarBaseInfoDriverApp selectByPrimaryKey(Long uuid);

    List<NetcarBaseInfoDriverApp> selectAll();

    int updateByPrimaryKey(NetcarBaseInfoDriverApp record);

    List<NetcarBaseInfoDriverApp> selectDriverAppList(@Param("companyId") String companyId, @Param("keyword") String keyword);

    int updateByPrimaryKeySelective(NetcarBaseInfoDriverApp record);

    List<NetcarBaseInfoDriverApp> selectDriverAppByLicenseId(@Param("companyId") String companyId, @Param("licenseId") String licenseId);
}