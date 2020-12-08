package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompanyService;
import com.zhcx.netcar.vo.CompanyNameVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;
@Mapper
public interface NetcarBaseInfoCompanyServiceMapper {

    List<NetcarBaseInfoCompanyService> selectAll();

    List<NetcarBaseInfoCompanyService> selectListByCompanyId(@Param("companyId") String companyId);

    int deleteByPrimaryKey(@Param("companyId") String companyId, @Param("address") Integer address);

    int insert(NetcarBaseInfoCompanyService record);

    int insertSelective(NetcarBaseInfoCompanyService record);

    NetcarBaseInfoCompanyService selectByPrimaryKey(@Param("companyId") String companyId, @Param("address") Integer address);

    int updateByPrimaryKeySelective(NetcarBaseInfoCompanyService record);

    int updateByPrimaryKey(NetcarBaseInfoCompanyService record);

    Long selectCountByCompanyId(@Param(value = "companyId") String companyId);

    Long selectCountByDistrictId(@Param("districtId") Long districtId);

    @Select("select address from netcar_base_info_company_service where company_id = #{companyId} ORDER BY create_date ASC LIMIT 1")
    int selectAddressById(String companyId);

    String selectCompanyNameByCompanyIdAndAddress(@Param("companyId") String companyId, @Param("address") Integer address);

    @Select("select address from netcar_base_info_company_service where company_id = #{companyId} ORDER BY create_date ASC LIMIT 1")
    Integer selectAddressByCompanyId(@Param("companyId") String companyId);

    List<CompanyNameVo> selectCompanyNameInYunzhengByCompanyId(@Param("companyId") String companyId, @Param("keyword") String keyword);

    List<CompanyNameVo> selectCompanyServiceNameByCompanyId(@Param("companyId") String companyId, @Param("keyword") String keyword);

//    @Select("select company_id from netcar_base_info_company_service where flag < 3 and service_no in (select identifier from t_base_info_company where corp_type = 4 and company_id != '')")
    @Select("select company_id from t_base_info_company where corp_type = 4 and company_id != ''")
    List<String> selectAllCompanyIdList();

    @Select("select company_id from t_base_info_company where corp_id = #{uuid}")
    String selectCompanyIdByYZuuid(@Param("uuid") Long uuid);

    @Select("select service_name from netcar_base_info_company_service where company_id = #{companyId} ORDER BY create_date ASC LIMIT 1")
    String selectCompanyNameByCompanyId(@Param("companyId") String companyId);

    NetcarBaseInfoCompanyService selectOneByCompanyId(String companyId);

    List<NetcarBaseInfoCompanyService> queryCompanyListByIds(@Param("corpIds") Set<String> corpIds);

    List<NetcarBaseInfoCompanyService> selectIllegalListByCompanyId(@Param("companyId") String companyId);

    @Select("select service_name from netcar_base_info_company_service WHERE company_id = (SELECT company_id from region_monitor_operate where uuid= #{regionId})  ORDER BY create_date ASC LIMIT 1")
    String selectCompanyNameByRegionId(@Param("regionId") Long regionId);
}