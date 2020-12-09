package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.app.NetcarPermitCompanyInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface NetcarPermitCompanyInfoMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarPermitCompanyInfo record);

    int insertSelective(NetcarPermitCompanyInfo record);

    NetcarPermitCompanyInfo selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarPermitCompanyInfo record);


    int updateByPrimaryKey(NetcarPermitCompanyInfo record);

    List<NetcarPermitCompanyInfo> selectPermitCompanyInfoList(@Param("corpId") Long corpId, @Param("flag") Integer flag, @Param("keyword") String keyword);

    @Select("select company_name from netcar_permit_company_info where status = 1 and flag = 1 and uuid = #{corpId}")
    String selectCompanyNameByPrimaryKey(@Param("corpId") Long corpId);

    @Select("select legal_phone from netcar_permit_company_info where status = 1 and flag = 1 and uuid = #{uuid}")
    String selectPhoneByUuid(Long uuid);
}