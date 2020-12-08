package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarBaseInfoDriver;
import com.zhcx.netcar.vo.BaseInfoEmpl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface NetcarBaseInfoDriverMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarBaseInfoDriver record);

    NetcarBaseInfoDriver selectByPrimaryKey(Long uuid);

    List<NetcarBaseInfoDriver> selectAll();

    int updateByPrimaryKey(NetcarBaseInfoDriver record);

    List<NetcarBaseInfoDriver> selectDriverList(@Param("companyId") String companyId, @Param("type") String type, @Param("keyword") String keyword);

    int updateByPrimaryKeySelective(NetcarBaseInfoDriver record);

    NetcarBaseInfoDriver selectDriverInfoByCompanyId(@Param("companyId") String companyId, @Param("licenseId") String licenseId);

    @Select("select license_id from netcar_base_info_driver where company_id = #{companyId}")
    List<String> queryLicenseIdListByCompanyId(String companyId);

    @Select("select address from t_base_info_empl where  company_id = #{companyId} and license_id = #{licenseId}")
    Integer selectAddressByCompanyIdAndLicenseId(@Param("companyId") String companyId, @Param("licenseId") String licenseId);

    List<NetcarBaseInfoDriver> selectIllegalDriverList(@Param("companyId") String companyId, @Param("type") String type, @Param("keyword") String keyword);

    List<NetcarBaseInfoDriver> getDriverIllegal(@Param("keyword") String keyword, @Param("type") String type, @Param("companyId") String companyId);

    void saveBatchEmpl(@Param("emplList")List<BaseInfoEmpl> insertEmplList);

    void updateBatchEmpl(@Param("driverExtendListList")List<BaseInfoEmpl> updateEmplList);
}