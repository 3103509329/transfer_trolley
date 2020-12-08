package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarShareCompany;
import com.zhcx.netcar.vo.CompanyNameVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface NetcarShareCompanyMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarShareCompany record);

    int insertSelective(NetcarShareCompany record);

    NetcarShareCompany selectByPrimaryKey(Long uuid);

    int updateByPrimaryKeySelective(NetcarShareCompany record);

    int updateByPrimaryKey(NetcarShareCompany record);

    List<NetcarShareCompany> queryShareCompanyList(@Param("companyId") String companyId, @Param("identifier") String identifier);

    List<CompanyNameVo> queryShareCompanyNameList(@Param("companyId") String companyId);

    @Select("select company_name from netcar_share_company where company_id = #{companyId} and address = #{address}")
    String selectCompanyNameByCompanyIdAndAddress(@Param("companyId") String companyId, @Param("address") Integer address);

}