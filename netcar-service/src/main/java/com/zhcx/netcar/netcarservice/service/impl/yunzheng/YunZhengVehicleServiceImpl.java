package com.zhcx.netcar.netcarservice.service.impl.yunzheng;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.yunzheng.YunZhengVehicleService;
import com.zhcx.netcar.netcarservice.mapper.yunzheng.YunZhengVehicleMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import com.zhcx.netcar.pojo.yuzheng.YunZhengVehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/5/12 14:00
 **/
@Service("yunZhengVehicleService")
public class YunZhengVehicleServiceImpl implements YunZhengVehicleService {
    @Autowired
    private YunZhengVehicleMapper yunZhengVehicleMapper;

    @Override
    public int insert(YunZhengVehicle yunZhengVehicle) {
        return yunZhengVehicleMapper.insert(yunZhengVehicle);
    }

    @Override
    public int updateByPrimaryKeySelective(YunZhengVehicle yunZhengVehicle) {
        return yunZhengVehicleMapper.updateByPrimaryKeySelective(yunZhengVehicle);
    }

    /**
     * 车辆新增（运政）
     *
     * @param yunZhengVehicle
     * @return
     */
    @Override
    public YunZhengVehicle addVehicle(YunZhengVehicle yunZhengVehicle) {
        yunZhengVehicle.setTime(new Date().toString());
        yunZhengVehicleMapper.insert(yunZhengVehicle);
        return yunZhengVehicle;
    }

    /**
     * 车辆查询（运政）
     *
     * @param vehicleNo
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    @Override
    public PageInfo<YunZhengVehicle> selectListByVehicleNo(String vehicleNo, String busiRegNumber, Integer pageNo, Integer pageSize, String orderBy) {
        PageHelper.startPage(pageNo, pageSize);
//        PageHelperUtil.orderBy(orderBy);

        List<YunZhengVehicle> vehicleList = yunZhengVehicleMapper.selectListByVehicleNo(vehicleNo, busiRegNumber);
        return new PageInfo<>(vehicleList);
    }

    /**
     * 车辆修改（运政）
     *
     * @param yunZhengVehicle
     * @return
     */
    @Override
    public YunZhengVehicle updateVehicle(YunZhengVehicle yunZhengVehicle) {
        yunZhengVehicleMapper.updateByPrimaryKeySelective(yunZhengVehicle);
        return yunZhengVehicle;
    }

    /**
     * 车辆删除（运政）
     *
     * @param vehicleNo
     * @param time
     * @return
     */
    @Override
    public Integer deleteVehicle(String vehicleNo, String time) {
        Integer i = yunZhengVehicleMapper.deleteByPrimaryKey(vehicleNo);
        return i;
    }

    /**
     * 车辆信息唯一性验证
     *
     * @param vehicleNo
     * @return
     */
    @Override
    public int selectByVehicleNo(String vehicleNo) {
        return yunZhengVehicleMapper.selectBycp(vehicleNo);
    }

    @Override
    public Long selectCountByCompanyId(String companyId) {
        return yunZhengVehicleMapper.selectCountByCompanyId(companyId);
    }

    @Override
    public int getTotal(String busiRegNumber) {
        return yunZhengVehicleMapper.getTotal(busiRegNumber);
    }

    /**
     * 基于公司获取旗下所有的车辆信息
     *
     * @param companyId
     * @return
     */
    @Override
    public List<YunZhengVehicle> selectListByCompanyId(String companyId, String vehicleNo) {
        return yunZhengVehicleMapper.selectListByCompanyId(companyId, vehicleNo);
    }

    /**
     * 添加车辆限定（基于运政数据统计计算）
     *
     * @return
     */
    @Override
    public List<YunZhengVehicle> filterVehicleNoCondition(List<String> companys) {
        List<YunZhengVehicle> result = yunZhengVehicleMapper.selectVehicleNo(companys);
        return result;
    }

    @Override
    public PageInfo<YunZhengVehicle> selectVehicle(String vehicleNo, Integer pageNo, Integer pageSize, String orderBy) {
        PageHelper.startPage(pageNo, pageSize);
        if (null != orderBy && !orderBy.equals("")) {
            PageHelperUtil.orderBy(orderBy);
        }
        return new PageInfo<>(yunZhengVehicleMapper.selectListByVehicleNo(vehicleNo, null));
    }
}
