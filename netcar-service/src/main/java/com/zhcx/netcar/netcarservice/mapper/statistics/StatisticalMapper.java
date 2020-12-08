package com.zhcx.netcar.netcarservice.mapper.statistics;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-09 11:13
 * 统计分数
 */
@Mapper
public interface StatisticalMapper {

    /**
     * 20岁以下的人
     *
     * @param year20
     * @return
     */
    @Select(" SELECT count(company_id) FROM t_base_info_empl WHERE birth_date > #{year20} and empl_type = 4 ")
    int getDriverByAge20(@Param("year20") String year20);

    /**
     * 20-40岁的人
     *
     * @param year20
     * @param year40
     * @return
     */
    @Select(" SELECT count(company_id) FROM t_base_info_empl WHERE birth_date <= #{year20} AND birth_date > #{year40} and empl_type = 4 ")
    int getDriverByAge20_40(@Param("year20") String year20, @Param("year40") String year40);

    /**
     * 40-60岁的人
     *
     * @param year40
     * @param year60
     * @return
     */
    @Select(" SELECT count(company_id) FROM t_base_info_empl WHERE birth_date <= #{year40} AND birth_date > #{year60} and empl_type = 4 ")
    int getDriverByAge40_60(@Param("year40") String year40, @Param("year60") String year60);

    /**
     * 60以上的人
     *
     * @param year60
     * @return
     */
    @Select(" SELECT count(company_id) FROM t_base_info_empl WHERE birth_date <= #{year60} and empl_type = 4 ")
    int getDriverByAge60(@Param("year60") String year60);

    /**
     * 根据月份查询当月取证的人员数量
     *
     * @param dateTime
     * @return
     */
    @Select(" SELECT count(company_id) FROM t_base_info_empl WHERE initial_work_time like #{dateTime} and empl_type = 4 ")
    int getDriverTotalByMonth(@Param("dateTime") String dateTime);

    /**
     * 根据月份查询当月取证的车辆数量
     *
     * @param dateTime
     * @return
     */
    @Select(" SELECT count(company_id) FROM t_base_info_vehiclelicence WHERE  rodetransport_start like #{dateTime}  and source = 4 and vehicle_type = 4 ")
    int getVehicleTotalByMonth(@Param("dateTime") String dateTime);
}

