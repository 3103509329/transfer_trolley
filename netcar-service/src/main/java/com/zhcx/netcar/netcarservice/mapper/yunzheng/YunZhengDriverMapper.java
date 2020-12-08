package com.zhcx.netcar.netcarservice.mapper.yunzheng;

import com.zhcx.netcar.pojo.yuzheng.YunZhengDriver;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface YunZhengDriverMapper {
    int deleteByPrimaryKey(String cardno);

    int insert(YunZhengDriver record);

    int insertSelective(YunZhengDriver record);

    YunZhengDriver selectByPrimaryKey(String cardno);

    int updateByPrimaryKeySelective(YunZhengDriver record);

    int updateByPrimaryKey(YunZhengDriver record);

    List<YunZhengDriver> selectByNameOrLicenseId(@Param("keyword") String keyword, @Param("busiRegNumber") String busiRegNumber);

    @Select("delete from yunzheng_base_info_driver where cardno = #{driverIdCard} and TIME = #{time}")
    Integer deleteByDriverIdCardAndTime(@Param("driverIdCard") String driverIdCard, @Param("time") String time);

    @Select("select count(1) from yunzheng_base_info_driver where cardno = #{driver_id_card}")
    int selectBylicenseId(String licenseId);

    @Select("select count(1) from yunzheng_base_info_driver")
    Long selectAll();

    Long selectCountByCompanyId(@Param("companyId") String companyId);

}