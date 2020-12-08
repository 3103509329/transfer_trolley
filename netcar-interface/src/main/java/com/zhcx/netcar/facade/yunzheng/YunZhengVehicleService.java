package com.zhcx.netcar.facade.yunzheng;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.yuzheng.YunZhengVehicle;

import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/5/12 14:00
 **/
public interface YunZhengVehicleService {
    int insert(YunZhengVehicle yunZhengVehicle);

    int updateByPrimaryKeySelective(YunZhengVehicle yunZhengVehicle);

    /**
     * 新增
     * @param yunZhengVehicle
     * @return
     */
    YunZhengVehicle addVehicle(YunZhengVehicle yunZhengVehicle);

    /**
     * 查询
     * @param vehicleNo
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    PageInfo<YunZhengVehicle> selectListByVehicleNo(String vehicleNo, String busiRegNumber, Integer pageNo, Integer pageSize, String orderBy);

    /**
     * 修改
     * @param yunZhengVehicle
     * @return
     */
    YunZhengVehicle updateVehicle(YunZhengVehicle yunZhengVehicle);

    /**
     * 删除
     * @param vehicleNo
     * @param time
     * @return
     */
    Integer deleteVehicle(String vehicleNo, String time);


    /**
     * 车辆信息唯一性验证
     * @param vehicleNo
     * @return
     */
    int selectByVehicleNo(String vehicleNo);

    Long selectCountByCompanyId(String companyId);

    int getTotal(String busiRegNumber);

    /**
     * 基于公司获取旗下所有的车辆信息
     * @param companyId
     * @return
     */
    List<YunZhengVehicle> selectListByCompanyId(String companyId, String vehicleNo);

    /**
     * 添加车辆限定（统计时，基于运政数据进行统计计算）
     * @return
     */
    List<YunZhengVehicle> filterVehicleNoCondition(List<String> companys);

    PageInfo<YunZhengVehicle> selectVehicle(String vehicleNo, Integer pageNo, Integer pageSize, String orderBy);
}
