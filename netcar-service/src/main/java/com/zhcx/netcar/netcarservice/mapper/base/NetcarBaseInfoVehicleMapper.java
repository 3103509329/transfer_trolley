package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.base.NetcarBaseInfoCompany;
import com.zhcx.netcar.pojo.base.NetcarBaseInfoVehicle;
import com.zhcx.netcar.vo.BaseInfoVehiclelicence;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface NetcarBaseInfoVehicleMapper {
    int deleteByPrimaryKey(Long uuid);

    int insert(NetcarBaseInfoVehicle record);

    NetcarBaseInfoVehicle selectByPrimaryKey(Long uuid);

    List<NetcarBaseInfoVehicle> selectAll();

    int updateByPrimaryKey(NetcarBaseInfoVehicle record);

    int updateByPrimaryKeySelective(NetcarBaseInfoVehicle record);

    NetcarBaseInfoVehicle selectByCompanyIdAndVehicleNo(@Param("companyId") String companyId, @Param("vehicleNo") String vehicleNo);

    List<NetcarBaseInfoVehicle> queryCompanyVehicleList(@Param("corpIds") List<String> corpIds);

    List<NetcarBaseInfoVehicle> selectVehicleBaseList(@Param("companyId") String companyId, @Param("vehicleNo") String vehicleNo);

    List<String> selectVehicleNoListByCompanyId(String companyId);

    List<NetcarBaseInfoVehicle> queryVehicleListByAddress(String address);

    @Select("select car_native_place from t_base_info_vehiclelicence where company_id = #{companyId} and car_num = #{vehicleNo}")
    Integer selectAddressByCompanyIdAndVehicleNo(@Param("companyId") String companyId, @Param("vehicleNo") String vehicleNo);

    List<NetcarBaseInfoVehicle> selectIllegalVehicleList(@Param("companyId")String companyId,@Param("vehicleNo") String vehicleNo);

    List<NetcarBaseInfoVehicle> getVehilceIllegal(@Param("companyId") String companyId, @Param("vehicleNo") String vehicleNo);

    List<NetcarBaseInfoCompany> getCompanyList();

    void saveBaseVehicleBatch(@Param("vehicles")List<BaseInfoVehiclelicence> insertBaseVehicleList);

    void updateBatchBaseVehicle(@Param("baseVehicelList")List<BaseInfoVehiclelicence> updateBaseVehicelList);
}