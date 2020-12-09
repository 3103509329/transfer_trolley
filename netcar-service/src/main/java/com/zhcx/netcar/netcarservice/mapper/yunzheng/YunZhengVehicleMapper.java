package com.zhcx.netcar.netcarservice.mapper.yunzheng;


import com.zhcx.netcar.pojo.yuzheng.YunZhengVehicle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface YunZhengVehicleMapper {
    int deleteByPrimaryKey(String branum);

    int insert(YunZhengVehicle record);

    int insertSelective(YunZhengVehicle record);

    YunZhengVehicle selectByPrimaryKey(String branum);

    int updateByPrimaryKeySelective(YunZhengVehicle record);

    int updateByPrimaryKey(YunZhengVehicle record);

    List<YunZhengVehicle> selectListByVehicleNo(@Param("vehicleNo") String vehicleNo, @Param("busiRegNumber") String busiRegNumber);

    @Select("select count(1) from yunzheng_base_info_vehicle where branum = #{vehicleNo}")
    int selectBycp(String vehicleNo);

    @Select("select count(1) from yunzheng_base_info_vehicle")
    Long selectAll();

    Long selectCountByCompanyId(@Param("busiregnumber") String busiregnumber);

    @Select(" SELECT count(branum) FROM yunzheng_base_info_vehicle WHERE busiRegNumber = #{busiRegNumber} ")
    int getTotal(@Param("busiRegNumber") String busiRegNumber);

    List<YunZhengVehicle> selectListByCompanyId(@Param("companyId") String companyId, @Param("vehicleNo") String vehicleNo);

    List<YunZhengVehicle> selectVehicleNo(@Param("vehicleNoList") List<String> vehicleNoList);
}