package com.zhcx.netcar.netcarservice.service.impl.base;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zhcx.netcar.netcarservice.mapper.cassandra.PositionRepository;
import com.zhcx.netcar.params.VehiclePositionParam;
import com.zhcx.netcar.pojo.base.NetcarPositionDriver;
import com.zhcx.netcar.pojo.base.NetcarPositionVehicle;
import com.zhcx.netcar.constant.EntityTypeEnum;
import com.zhcx.netcar.constant.KeyEnum;
import com.zhcx.netcar.facade.base.CompanyServiceService;
import com.zhcx.netcar.facade.base.PositionService;
import com.zhcx.netcar.netcarservice.utils.DateTimeUtil;
import com.zhcx.netcar.netcarservice.canssandra.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/12/19 14:14
 **/
@Service("positionService")
public class PositionServiceImpl implements PositionService {

    private PositionRepository positionRepository;

    @Resource(name = "redisTemplate4Json")
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private CompanyServiceService companyServiceService;

    @Autowired
    public void setPositionRepository(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Override
    public List<NetcarPositionVehicle> queryHistoryPosition(String vehicleNo, Long ts) throws Exception {

        if (null == ts) {
            ts = System.currentTimeMillis();
        }
        Long partition = DateTimeUtil.getCurrentMonthPartitionTime(ts, null);

        Long startTimestamp = DateTimeUtil.getStartTimeOfDay(ts, null);
        Long endTimestamp = DateTimeUtil.getEndTimeOfDay(ts, null);
        List<Position> positionList = positionRepository.selectListByCondition(partition, EntityTypeEnum.VEHICLE.getCode(), vehicleNo, KeyEnum.FULL.getCode(), startTimestamp, endTimestamp);
//
        List<NetcarPositionVehicle> list = Lists.newArrayList();
        for (Position position : positionList) {
            if (null != position) {
                NetcarPositionVehicle netcarPositionVehicle = JSON.parseObject(position.getStrV(), NetcarPositionVehicle.class);
                String positionTime = DateTimeUtil.getYMDHMSFormat(new Date(position.getPositionKey().getTs()));
                netcarPositionVehicle.setPositionTime(positionTime);
                netcarPositionVehicle.setVehicleNo(position.getPositionKey().getEntityId());
                String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(netcarPositionVehicle.getCompanyId(), netcarPositionVehicle.getVehicleRegionCode());
                netcarPositionVehicle.setCompanyName(companyName);
                list.add(netcarPositionVehicle);
            }
        }
        return list;
    }

    @Override
    public NetcarPositionVehicle queryRealTimePosition(String vehicleNo) throws Exception {
        Position position = positionRepository.selectLatestPositionByEntityId(EntityTypeEnum.VEHICLE.getCode(), vehicleNo, KeyEnum.FULL.getCode());
        if (null == position) {
            return null;
        }
        NetcarPositionVehicle netcarPositionVehicle = JSON.parseObject(position.getStrV(), NetcarPositionVehicle.class);
        String positionTime = DateTimeUtil.getYMDHMSFormat(new Date(position.getPositionKey().getTs()));
        netcarPositionVehicle.setPositionTime(positionTime);
        netcarPositionVehicle.setVehicleNo(vehicleNo);
        return netcarPositionVehicle;
    }

    @Override
    public List<NetcarPositionDriver> queryDriverPosition(String licenseId, Long ts) {
        if (null == ts) {
            ts = System.currentTimeMillis();
        }
        Long partition = DateTimeUtil.getCurrentMonthPartitionTime(ts, null);
        Long startTimestamp = DateTimeUtil.getStartTimeOfDay(ts, null);
        Long endTimestamp = DateTimeUtil.getEndTimeOfDay(ts, null);
        List<Position> positions = positionRepository.selectListByCondition(partition, EntityTypeEnum.DRIVER.getCode(), licenseId, KeyEnum.FULL.getCode(), startTimestamp, endTimestamp);

        List<NetcarPositionDriver> list = Lists.newArrayList();
        for (Position position : positions) {
            if (null != position) {
                NetcarPositionDriver netcarPositionDriver = JSON.parseObject(position.getStrV(), NetcarPositionDriver.class);
                String positionTime = DateTimeUtil.getYMDHMSFormat(new Date(position.getPositionKey().getTs()));
                netcarPositionDriver.setPositionTime(positionTime);
                netcarPositionDriver.setVehicleNo(position.getPositionKey().getEntityId());
                String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(netcarPositionDriver.getCompanyId(), netcarPositionDriver.getDriverRegionCode());
                netcarPositionDriver.setCompanyName(companyName);
                list.add(netcarPositionDriver);
            }
        }
        return list;
    }

    @Override
    public int selectList(String vehicleNo, Long ts) throws ParseException {
        if (null == ts) {
            ts = System.currentTimeMillis();
        }
        Long partition = DateTimeUtil.getCurrentMonthPartitionTime(ts, null);

        Long startTimestamp = DateTimeUtil.getStartTimeOfDay(ts, null);
        Long endTimestamp = DateTimeUtil.getEndTimeOfDay(ts, null);
        List<Position> positionList = positionRepository.selectListByCondition(partition, EntityTypeEnum.VEHICLE.getCode(), vehicleNo, KeyEnum.FULL.getCode(), startTimestamp, endTimestamp);

        List<NetcarPositionVehicle> list = Lists.newArrayList();
        //将数据添加到redis进行分页查询
        //添加的数据是用时间戳作为redis有序集合的分数，
        //可以通过redis自带的方法对现有的集合进行排序等操作
        BoundZSetOperations boundZSetOperations = redisTemplate.boundZSetOps(vehicleNo);
        boundZSetOperations.expire(60, TimeUnit.MINUTES);
        int total = positionList.size();

        for (int index = 0; index < total; index++) {
            Position position = positionList.get(index);
            NetcarPositionVehicle netcarPositionVehicle = JSON.parseObject(position.getStrV(), NetcarPositionVehicle.class);
            String positionTime = DateTimeUtil.getYMDHMSFormat(new Date(position.getPositionKey().getTs()));
            netcarPositionVehicle.setPositionTime(positionTime);
            netcarPositionVehicle.setVehicleNo(vehicleNo);
            list.add(netcarPositionVehicle);
            String value = JSON.toJSONString(netcarPositionVehicle);
            boundZSetOperations.add(value, index);
        }
        return total;
    }


    /**
     * 获取指定时间区间的轨迹数据，时间区间来源于订单的开始时间和结束时间
     *
     * @param vehiclePositionParam
     * @return
     */
    @Override
    public List queryOrderHistoryPosition(VehiclePositionParam vehiclePositionParam) {
        List<Position> positionList = new ArrayList<>();
        if (vehiclePositionParam != null) {
            Long partition = DateTimeUtil.getCurrentMonthPartitionTime(vehiclePositionParam.getTs(), null);
            String vehicleNo = vehiclePositionParam.getVehicleNo();
            Long startTimestamp = vehiclePositionParam.getStartTime();
            Long endTimestamp = vehiclePositionParam.getEndTime();
            positionList = positionRepository.selectListByCondition(partition, EntityTypeEnum.VEHICLE.getCode(), vehicleNo, KeyEnum.FULL.getCode(), startTimestamp, endTimestamp);
        } else {
            return null;
        }
        return positionList;
    }
}
