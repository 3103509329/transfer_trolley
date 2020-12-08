package com.zhcx.netcar.netcarservice.mapper.workbench;

import com.zhcx.netcar.pojo.workbech.WorkbenchDriver;
import com.zhcx.netcar.pojo.workbech.WorkbenchValue;
import com.zhcx.netcar.pojo.workbech.WorkbenchVehicle;
import com.zhcx.netcar.pojo.yuzheng.YunZhengVehicle;
import com.zhcx.netcar.vo.*;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-15 15:31
 * 工作台
 */
@Mapper
public interface WorkbenchMapper {

    /**
     * 获取运政企业信息
     *
     * @return
     * @throws Exception
     */
    @Select(" SELECT t1.company_name as clitname ," +
            " (SELECT t2.company_id FROM netcar_base_info_company_service as t2 WHERE t2.service_no =  t1.identifier) as companyId, t1.identifier as busiregnumber," +
            " t1.identifier,t1.corp_id as uuid,t1.egal_rep_name as personLiable,t1.phone FROM t_base_info_company as t1 where  t1.source = 4 and t1.corp_type = 4 ")
    List<Map<String, Object>> getEnterprise();


    /**
     * 根据企业获取运政的所有车辆
     *
     * @param busiregnumber
     * @return
     */
    @Select(" SELECT t1.car_num as branum FROM t_base_info_vehiclelicence as t1 WHERE t1.company_id = (select t2.company_id from netcar_base_info_company_service as t2 where t2.service_no = #{busiregnumber}) and t1.vehicle_type = 4 and t1.source = 4")
    List<String> getYunZhengVehicleByBusiRegNumber(@Param("busiregnumber") String busiregnumber);

    /**
     * 根据企业获取部级的所有车辆
     *
     * @param busiregnumber
     * @return
     */
    @Select(" SELECT t1.car_num as vehicle_no FROM t_base_info_vehiclelicence as t1 WHERE t1.company_id = (select t2.company_id from netcar_base_info_company_service as t2 where t2.service_no = #{busiregnumber}) and t1.vehicle_type = 4")
    List<String> getVehicleByCompanyId(@Param("busiregnumber") String busiregnumber);

    /**
     * 查询企业的所有运营车辆
     *
     * @param busiregnumber
     * @return
     */
    @Select(" SELECT DISTINCT t1.vehicle_no FROM netcar_operate_depart as t1 WHERE t1.company_id = (select t2.company_id from netcar_base_info_company_service as t2 where t2.service_no = #{busiregnumber}) ")
    List<String> getOperatingVehicleByCompanyId(@Param("busiregnumber") String busiregnumber);

    /**
     * 车辆里程超限车辆数
     *
     * @param mileage       里程
     * @param busiregnumber
     * @return
     */
    @Select(" SELECT " +
            " t1.vehicle_no  " +
            "FROM " +
            " netcar_base_info_vehicle_total_mile as t1 " +
            " JOIN t_base_info_vehiclelicence as t2 ON t1.vehicle_no = t2.car_num  " +
            "WHERE " +
            " t2.company_id = (select t3.company_id from netcar_base_info_company_service as t3 where t3.service_no = #{busiregnumber} ) AND t1.total_mile > #{mileage} and t2.source = 4 and t2.vehicle_type = 4")
    List<String> getMileageTransfiniteByCompanyId(@Param("mileage") Integer mileage, @Param("busiregnumber") String busiregnumber);

    /**
     * 保险到期车辆数
     *
     * @param dateTime
     * @param busiregnumber
     * @return
     */
    @Select(" SELECT " +
            " t1.vehicle_no  " +
            "FROM " +
            " netcar_base_info_vehicle_insurance AS t1 " +
            " JOIN t_base_info_vehiclelicence AS t2 ON t1.vehicle_no = t2.car_num  " +
            "WHERE " +
            " t2.company_id = ( SELECT t3.company_id FROM netcar_base_info_company_service AS t3 WHERE t3.service_no = #{busiregnumber}) AND t1.insur_exp < #{dateTime} AND t2.source = 4 and t2.vehicle_type = 4")
    List<String> getInsuranceIsDueByCompanyId(@Param("dateTime") String dateTime, @Param("busiregnumber") String busiregnumber);

    /**
     * 保险金额不合格
     *
     * @param insurCount    保险金额
     * @param busiregnumber
     * @return
     */
    @Select("SELECT  " +
            " t1.vehicle_no  " +
            "FROM " +
            " netcar_base_info_vehicle_insurance AS t1 " +
            " JOIN t_base_info_vehiclelicence as t2 ON t1.vehicle_no = t2.car_num  " +
            "WHERE " +
            " t2.company_id = ( SELECT t3.company_id FROM netcar_base_info_company_service AS t3 WHERE t3.service_no = #{busiregnumber} ) AND t1.insur_count < #{insurCount} AND t2.source = 4 and t2.vehicle_type = 4")
    List<String> getInsuredAmountUnqualifiedByCompanyId(@Param("insurCount") Double insurCount, @Param("busiregnumber") String busiregnumber);

    /**
     * 无车载终端
     *
     * @param busiregnumber
     * @return
     */
    @Select("SELECT  " +
            " t2.car_num as vehicle_no  " +
            "FROM  " +
            "t_base_info_vehiclelicence t2  " +
            "WHERE " +
            "t2.company_id = ( SELECT t3.company_id FROM netcar_base_info_company_service AS t3 WHERE t3.service_no =  #{busiregnumber} ) AND (t2.whether_gps = 2 OR t2.whether_gps IS  NULL) and t2.vehicle_type = 4 and t2.source = 4")
    List<String> getNotCarTerminalByCompanyId(@Param("busiregnumber") String busiregnumber);


    /**
     * 年检过期的车辆数
     *
     * @param dateTime
     * @param busiregnumber
     * @return
     */
    @Select("SELECT " +
            " t2.car_num as vehicle_no    " +
            " FROM " +
            " t_base_info_vehiclelicence t2    " +
            " WHERE   " +
            " t2.company_id = ( SELECT t3.company_id FROM netcar_base_info_company_service AS t3 WHERE t3.service_no =   #{busiregnumber}) AND t2.rodetransport_end < #{dateTime} and t2.vehicle_type = 4 and t2.source = 4")
    List<String> getAnnualInspectionOverdueByCompanyId(@Param("dateTime") String dateTime, @Param("busiregnumber") String busiregnumber);


    /**
     * 添加监管统计信息
     *
     * @param workbenchVehicle
     * @return
     */
    @Insert(" INSERT INTO netcar_workbench_vehicle(enterprise_name,head,phone,not_active,not_business_180,annual_inspection_overdue,not_terminal, " +
            " insured_amount_unqualified,insurance_due,mileage_transfinite,not_operating,lack_data,company_id,busiRegNumber,statistical_time,excel_url) " +
            " VALUES(#{enterpriseName},#{head},#{phone},#{notActive},#{notBusiness180},#{annualInspectionOverdue},#{notTerminal}," +
            " #{insuredAmountUnqualified},#{insuranceDue},#{mileageTransfinite},#{notOperating},#{lackData},#{companyId},#{busiRegNumber},now(),#{excelUrl}) ")
    int saveWorkbenchVehicle(WorkbenchVehicle workbenchVehicle);

    /**
     * 清空监管统计表
     *
     * @return
     */
    @Delete(" DELETE FROM netcar_workbench_vehicle ")
    int deleteWorkbenchVehicle();

    @Select("<script>" +
            " SELECT " +
            " busiregnumber,branum,owner_name as ownerName,traword , trano,factype , model, stadate, enddate " +
            " FROM yunzheng_base_info_vehicle WHERE 1 = 1 " +
            " <if test=\"vehicleNoList != null \"> " +
            " AND branum in  " +
            " <foreach collection=\" vehicleNoList \"  item= \"vehicleNo\"  index= \"index\"  open= \"( \" close=\" ) \" separator=\" ,\" > " +
            "   #{vehicleNo} " +
            " </foreach>" +
            " </if> " +
            "</script> ")
    List<YunZhengVehicle> getYunZhengVehicle(@Param("vehicleNoList") List<String> vehicleNoList);

    @Select("<script>" +
            " SELECT " +
            " #{busiregnumber} as busiregnumber,t1.car_num as branum, t1.roadtransport_img as trano" +
            " FROM t_base_info_vehiclelicence as t1  WHERE 1 = 1 and t1.company_id IN ( SELECT t2.company_id FROM netcar_base_info_company_service AS t2 WHERE t2.service_no =#{busiregnumber})  and t1.source = 4 and t1.vehicle_type = 4 " +
            " <if test=\"vehicleNoList != null \"> " +
            " AND t1.car_num in  " +
            " <foreach collection= \"vehicleNoList\"  item= \"vehicleNo\"  index= \"index\"  open= \"(\"  close= \")\"  separator= \",\" > " +
            "   #{vehicleNo} " +
            " </foreach>" +
            " </if> " +
            "</script> ")
    List<VehicleInfoLack> getYunZhengVehicleLack(@Param("vehicleNoList") List<String> vehicleNoList, @Param("busiregnumber") String busiregnumber);

    @Select("<script>" +
            " SELECT t1.car_num as branum, t1.roadtransport_img as trano" +
            " FROM t_base_info_vehiclelicence as t1 WHERE 1 = 1 and t1.company_id IN ( SELECT t2.company_id FROM netcar_base_info_company_service AS t2 WHERE t2.service_no =#{busiregnumber})  and t1.source = 4 and t1.vehicle_type = 4 " +
            " <if test=\"vehicleNoList != null \"> " +
            " AND t1.car_num in  " +
            " <foreach collection= \"vehicleNoList\"  item= \"vehicleNo\"  index= \"index\"  open= \"(\"  close= \")\"  separator= \",\" > " +
            "   #{vehicleNo} " +
            " </foreach>" +
            " </if> " +
            "</script> ")
    List<VehicleNotOperating> getYunZhengVehicleNotOperating(@Param("vehicleNoList") List<String> vehicleNoList, @Param("busiregnumber") String busiregnumber);

    /**
     * 获取里程超限
     *
     * @param vehicleNoList
     * @return
     */
    @Select("<script>" +
            " SELECT " +
            " t1.car_num AS vehicleNo, " +
            " vtm.total_mile AS totalMile  " +
            "FROM " +
            " t_base_info_vehiclelicence AS t1 " +
            " JOIN netcar_base_info_vehicle_total_mile vtm ON t1.car_num = vtm.vehicle_no  " +
            "WHERE " +
            " 1 = 1  " +
            " AND t1.company_id IN ( SELECT t2.company_id FROM netcar_base_info_company_service AS t2 WHERE t2.service_no =#{busiregnumber})  and t1.source = 4 and t1.vehicle_type = 4 " +
            " <if test=\"vehicleNoList != null \"> " +
            " AND t1.car_num in  " +
            " <foreach collection= \"vehicleNoList\"  item= \"vehicleNo\"  index= \"index\"  open= \"(\"  close= \")\"  separator= \",\" > " +
            "   #{vehicleNo} " +
            " </foreach>" +
            " </if> " +
            "</script>  ")
    List<VehicleMileageBeyond> getNetcarBaseInfoByMile(@Param("vehicleNoList") List<String> vehicleNoList, @Param("busiregnumber") String busiregnumber);

    /**
     * 获取保险金额/保险过期
     *
     * @param vehicleNoList
     * @return
     */
    @Select("<script>" +
            "SELECT " +
            " t1.car_num AS vehicleNo, " +
            " ivi.insur_exp AS insurExp, " +
            " ivi.insur_count AS insurCount, " +
            " ivi.insur_type AS insurType  " +
            "FROM " +
            " t_base_info_vehiclelicence AS t1 " +
            " JOIN netcar_base_info_vehicle_insurance AS ivi ON t1.car_num = ivi.vehicle_no  " +
            "WHERE " +
            " 1 = 1  " +
            " AND t1.company_id IN ( SELECT t2.company_id FROM netcar_base_info_company_service AS t2 WHERE t2.service_no = #{busiregnumber})  " +
            " AND t1.source = 4 and t1.vehicle_type = 4 " +
            " <if test=\"vehicleNoList != null \"> " +
            " AND t1.car_num in  " +
             " <foreach collection= \"vehicleNoList\"  item= \"vehicleNo\"  index= \"index\"  open= \"(\"  close= \")\"  separator= \",\" > " +
            "   #{vehicleNo} " +
            " </foreach>" +
            " </if> " +
            "</script>  ")
    List<VehicleInsurExpired> getNetcarBaseInfoByInsurance(@Param("vehicleNoList") List<String> vehicleNoList, @Param("busiregnumber") String busiregnumber);

    /**
     * 获取车辆信息
     *
     * @param vehicleNoList
     * @return
     */
    @Select("<script>" +
            " SELECT " +
            " t1.car_num AS branum, " +
            " t1.vin AS gpsInstallDate, " +
            " t1.ein AS gpsImei  " +
            "FROM " +
            " t_base_info_vehiclelicence AS t1  " +
            "WHERE " +
            " 1 = 1  " +
            " AND t1.company_id IN ( SELECT t2.company_id FROM netcar_base_info_company_service AS t2 WHERE t2.service_no = #{busiregnumber} ) AND t1.source = 4 and t1.vehicle_type = 4 " +
            " <if test=\"vehicleNoList != null \"> " +
            " AND t1.car_num  in  " +
             " <foreach collection= \"vehicleNoList\"  item= \"vehicleNo\"  index= \"index\"  open= \"(\"  close= \")\"  separator= \",\" > " +
            "   #{vehicleNo} " +
            " </foreach>" +
            " </if> " +
            "</script>  ")
    List<VehicleNotTerminal> getNetcarBaseInfoNotTerminal(@Param("vehicleNoList") List<String> vehicleNoList, @Param("busiregnumber") String busiregnumber);

    /**
     * 获取车辆信息
     *
     * @param vehicleNoList
     * @return
     */
    @Select("<script>" +
            " SELECT " +
            " t1.car_num AS vehicleNo, " +
            " t1.rodetransport_end AS nextFixDate " +
            " FROM " +
            " t_base_info_vehiclelicence AS t1 " +
            " WHERE " +
            " 1 = 1 " +
            " AND t1.company_id IN ( SELECT t2.company_id FROM netcar_base_info_company_service AS t2 WHERE t2.service_no =  #{busiregnumber}) and t1.source = 4 and t1.vehicle_type = 4 " +
            " <if test=\"vehicleNoList != null \"> " +
            " AND t1.car_num in  " +
             " <foreach collection= \"vehicleNoList\"  item= \"vehicleNo\"  index= \"index\"  open= \"(\"  close= \")\"  separator= \",\" > " +
            "   #{vehicleNo} " +
            " </foreach>" +
            " </if> " +
            "</script>  ")
    List<VehicleAnnualInspectionExpired> getVehicleAnnualInspectionExpired(@Param("vehicleNoList") List<String> vehicleNoList, @Param("busiregnumber") String busiregnumber);

    /**
     * 获取车辆监管信息
     *
     * @return
     */
    @Select(" SELECT uuid,enterprise_name as enterpriseName,head,phone,not_active as notActive,not_business_180 as notBusiness180, " +
            " annual_inspection_overdue as annualInspectionOverdue,not_terminal as notTerminal,insured_amount_unqualified as insuredAmountUnqualified, " +
            " insurance_due as insuranceDue,mileage_transfinite as mileageTransfinite,not_operating as notOperating, " +
            " lack_data as lackData,company_id as companyId,busiRegNumber,statistical_time as statisticalTime,excel_url as excelUrl FROM netcar_workbench_vehicle ")
    List<WorkbenchVehicle> getVehicleMonitoring();

    /**
     * 获取驾驶员监管信息
     *
     * @return
     */
    @Select(" SELECT uuid,enterprise_name as enterpriseName,head,phone,license_overdue as licenseOverdue, " +
            " age_excessive as ageExcessive,not_mount_guard as notMountGuard,lack_data as lackData,company_id as  companyId," +
            " busiRegNumber,statistical_time as statisticalTime,excel_url as excelUrl FROM netcar_workbench_driver ")
    List<WorkbenchDriver> getDriverMonitoring();

    /**
     * 删除驾驶员监管信息
     *
     * @return
     */
    @Delete(" DELETE FROM netcar_workbench_driver ")
    int deleteWorkbenchDriver();

    /**
     * 获取驾照过期
     *
     * @param busiregnumber
     * @param dateTime
     * @return
     */
    @Select(" SELECT bid.license_id FROM t_base_info_empl bid " +
            " WHERE bid.company_id = (SELECT t2.company_id FROM netcar_base_info_company_service AS t2 WHERE t2.service_no =  #{busiregnumber}) AND bid.driver_license_off < #{dateTime} and bid.empl_type =4 and bid.source = 4")
    List<String> getLicenseOverdueByCompanyId(@Param("busiregnumber") String busiregnumber, @Param("dateTime") String dateTime);

    /**
     * 年龄超限
     *
     * @param busiregnumber
     * @param dateTime
     * @return
     */
    @Select("select t1.license_id from t_base_info_empl as t1 where t1.birth_date  < #{dateTime} and t1.company_id in (SELECT t2.company_id FROM netcar_base_info_company_service AS t2 WHERE t2.service_no =  #{busiregnumber}) and t1.source = 4 and t1.empl_type = 4 ")
    List<String> getAgeExcessiveList(@Param("busiregnumber") String busiregnumber, @Param("dateTime") String dateTime);

    /**
     * 该公司驾驶员(运政和部级同时存在）
     *
     * @param busiregnumber
     * @return
     */
    @Select("select t1.license_id from t_base_info_empl as t1 where t1.company_id in( SELECT t2.company_id FROM netcar_base_info_company_service AS t2 WHERE t2.service_no =  #{busiregnumber}) and t1.source = 4 and t1.empl_type = 4 ")
    List<String> getYunzhengDriver(@Param("busiregnumber") String busiregnumber);

    /**
     * 该公司所有的部级驾驶人员
     *
     * @param busiregnumber
     * @return
     */
    @Select(" SELECT t1.license_id FROM t_base_info_empl as t1 WHERE t1.company_id = (SELECT t2.company_id FROM netcar_base_info_company_service AS t2 WHERE t2.service_no =  #{busiregnumber})")
    List<String> getNetCarDriver(@Param("busiregnumber") String busiregnumber);

    /**
     * 查询该公司订单表的驾驶员
     *
     * @param busiregnumber
     * @return
     */
    @Select(" SELECT DISTINCT license_id FROM netcar_operate_pay WHERE company_id = (select company_id from netcar_base_info_company_service where service_no = #{busiregnumber})")
    List<String> getOrderDriver(@Param("busiregnumber") String busiregnumber);

    /**
     * 获取部级表驾驶员驾照过期
     *
     * @param licenseList
     * @return
     */
    @Select("<script>" +
            " SELECT t1.name as driverName,t1.phone_no as driverPhone," +
            " t1.license_id as licenseId, t1.driver_license_off as driverLicenseOff FROM t_base_info_empl as t1" +
            " WHERE 1 = 1 and t1.company_id in (select t2.company_id from netcar_base_info_company_service as t2 where t2.service_no = #{busiRegNumber}) and t1.empl_type = 4 " +
            " <if test=\"licenseList != null \"> " +
            " AND t1.license_id in  " +
            " <foreach collection=\"licenseList\" item=\"licenseId\" index=\"index\" open=\"(\" close=\")\" separator=\",\"> " +
            "   #{licenseId} " +
            " </foreach>" +
            " </if> " +
            "</script>  ")
    List<DriverLicenseExpired> getNetcarBaseInfoDriverExpired(@Param("licenseList") List<String> licenseList, @Param("busiRegNumber") String busiRegNumber);

    /**
     * 获取部级表驾驶员信息年龄过大
     *
     * @param licenseList
     * @return
     */
    @Select("<script>" +
            " SELECT t1.name as driverName,t1.phone_no as driverPhone,t1.license_id as licenseId FROM t_base_info_empl as t1" +
            " WHERE 1 = 1 and t1.company_id in (select t2.company_id from netcar_base_info_company_service as t2 where t2.service_no = #{busiRegNumber})  and t1.source = 4 and t1.empl_type = 4 " +
            " <if test=\"licenseList != null \"> " +
            " AND license_id in  " +
            " <foreach collection=\"licenseList\" item=\"licenseId\" index=\"index\" open=\"(\" close=\")\" separator=\",\"> " +
            "   #{licenseId} " +
            " </foreach>" +
            " </if> " +
            "</script>  ")
    List<DriverAgeBeyond> getNetcarBaseInfoDriverAgeBeyond(@Param("licenseList") List<String> licenseList, @Param("busiRegNumber") String busiRegNumber);


    /**
     * 获取运政表驾驶员信息
     *
     * @param licenseList
     * @return
     */
    @Select("<script>" +
            " SELECT name, idc_num as cardno" +
            " FROM t_base_info_empl yid  " +
            " WHERE 1 = 1 and yid.company_id = (select t2.company_id from netcar_base_info_company_service as t2 where t2.service_no = #{busiRegNumber}) " +
            " <if test=\"licenseList != null \"> " +
            " AND yid.idc_num in  " +
            " <foreach collection=\"licenseList\" item=\"licenseId\" index=\"index\" open=\"(\" close=\")\" separator=\",\"> " +
            "   #{licenseId} " +
            " </foreach>" +
            " </if> " +
            "</script>  ")
    List<DriverInfoLack> getYunZhengDriverLack(@Param("licenseList") List<String> licenseList, @Param("busiRegNumber") String busiRegNumber);

    /**
     * 考证未上岗
     *
     * @param licenseList
     * @param busiRegNumber
     * @return
     */
    @Select("<script>" +
            " SELECT t1.NAME, " +
            " t1.idc_num as cardno, " +
            " t1.certificate_no AS certificateNo, " +
            " t1.phone_no AS driverPhone, " +
            " t1.network_car_issue_organization AS networkCarIssueOrganization, " +
            " t1.network_car_issue_date AS networkCarIssueDate  " +
            "FROM " +
            " t_base_info_empl  as t1 " +
            "WHERE " +
            " 1 = 1  " +
            " AND t1.company_id = ( select t2.company_id from netcar_base_info_company_service as t2 where t2.service_no = #{busiRegNumber} ) and t1.source = 4 and t1.empl_type = 4 " +
            " <if test=\"licenseList != null \"> " +
            " AND t1.idc_num in  " +
            " <foreach collection=\"licenseList\" item=\"licenseId\" index=\"index\" open=\"(\" close=\")\" separator=\",\"> " +
            "   #{licenseId} " +
            " </foreach>" +
            " </if> " +
            "</script>  ")
    List<DriverNotOperating> getYunZhengDriverNotOperating(@Param("licenseList") List<String> licenseList, @Param("busiRegNumber") String busiRegNumber);

    /**
     * 添加统计数据
     *
     * @param workbenchDriver
     * @return
     */
    @Insert(" INSERT INTO netcar_workbench_driver(company_id,busiRegNumber,enterprise_name,head,phone,license_overdue," +
            " age_excessive,not_mount_guard,lack_data,statistical_time,excel_url) " +
            " VALUES(#{companyId},#{busiRegNumber},#{enterpriseName},#{head},#{phone},#{licenseOverdue}," +
            " #{ageExcessive},#{notMountGuard},#{lackData},now(),#{excelUrl}) ")
    int saveWorkbenchDriver(WorkbenchDriver workbenchDriver);

    /**
     * 删除所有的车牌和驾驶证信息
     */
    @Delete(" DELETE FROM netcar_workbench_value ")
    void deleteValue();

    /**
     * 添加车牌和驾驶证信息
     *
     * @return
     */
    @Insert(" <script>" +
            " INSERT INTO netcar_workbench_value(busiRegNumber,type,`value`) " +
            " VALUES" +
            " <foreach collection=\"list\" index=\"index\" item=\"item\" separator=\",\">\n" +
            "    (#{item.busiRegNumber},#{item.type},#{item.value})" +
            " </foreach>" +
            " </script>")
    int insertValue(List<WorkbenchValue> list);

    /**
     * 年检过期
     *
     * @param type 类型 1部平台车辆数据缺失 2已取证未运营车辆数  3车辆里程超限车辆数 4保险到期车辆数 5保险金额不合格
     *             6无车载终端 7年检过期的车辆数 8驾照过期 9年龄超大 10持证未上岗 11部平台驾驶员信息缺失
     * @return
     */
    @Select(" SELECT `value` FROM netcar_workbench_value WHERE type = #{type} and busiRegNumber = #{busiRegNumber} ")
    List<String> getValueByType(@Param("busiRegNumber") String busiRegNumber, @Param("type") Integer type);

    /**
     * 查询运政所有的车牌
     *
     * @param busiregnumber
     * @return
     */
    @Select("SELECT t1.idc_num FROM t_base_info_empl as t1 WHERE t1.company_id =  ( select t2.company_id from t_base_info_company as t2 where t2.identifier = #{busiregnumber} order by t2.establishment_date LIMIT 1) and t1.empl_type = 4 and t1.source = 4")
    List<String> selectAllYunzhengDriverList(@Param("busiregnumber") String busiregnumber);
}
