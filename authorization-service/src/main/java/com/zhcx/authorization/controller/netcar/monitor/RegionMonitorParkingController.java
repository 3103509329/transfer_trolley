package com.zhcx.authorization.controller.netcar.monitor;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.zhcx.authorization.config.SystemControllerLog;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.regionmonitor.constant.MonitorRedisConst;
import com.zhcx.regionmonitor.monitor.RegionMonitorParkingService;
import com.zhcx.regionmonitor.pojo.RegionMonitorParking;
import com.zhcx.regionmonitor.response.MonitorResult;
import com.zhcx.regionmonitor.response.RegionAlarmInfo;
import com.zhcx.regionmonitor.response.RegionMonitor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2018/12/5 0005 16:04
 **/
@RestController
@RequestMapping("/monitor/parking")
@Api(value = "parking", tags = "超时逗留（电子围栏信息）")
public class RegionMonitorParkingController {

    private Logger log = LoggerFactory.getLogger(RegionMonitorParkingController.class);

    @Autowired
    private RegionMonitorParkingService regionMonitorParkingService;

    @Resource(name = "redisTemplate4Json")
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 新增
     * @param regionMonitorParking
     * @return
     */
    @SystemControllerLog(description = "超时逗留新增")
    @ApiOperation(value = "超时逗留新增", notes = "")
    @PostMapping
    public MessageResp addRegionMonitorParking(@RequestBody RegionMonitorParking regionMonitorParking) {

        MessageResp messageResp = new MessageResp();
        PageInfo<RegionMonitorParking> regionMonitorParkingPageInfo = null;
        try {
            regionMonitorParkingPageInfo = regionMonitorParkingService.addRegionMonitorParking(regionMonitorParking);
            if (null == regionMonitorParkingPageInfo.getList()) {
                messageResp.setStatusCode("-50");
                messageResp.setResult(Boolean.FALSE.toString());
                messageResp.setResultDesc("新增异常");
            } else {
                messageResp.setData(regionMonitorParkingPageInfo.getList());
                messageResp.setResult(Boolean.TRUE.toString());
                messageResp.setResultDesc("新增成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("新增异常");
        }
        return messageResp;
    }

    /**
     * 删除
     * @param
     * @return
     */
    @SystemControllerLog(description = "超时逗留删除")
    @ApiOperation(value = "超时逗留删除", notes = "")
    @DeleteMapping("/{uuid}")
    public MessageResp deleteRegionMonitorParking(@PathVariable Long uuid) {

        MessageResp messageResp = new MessageResp();
        try {
            int i = regionMonitorParkingService.deleteRegionMonitorParking(uuid);
            if (-1 == i) {
                messageResp.setStatusCode("-50");
                messageResp.setResult(Boolean.FALSE.toString());
                messageResp.setResultDesc("删除异常");
            } else {
                messageResp.setResult(Boolean.TRUE.toString());
                messageResp.setResultDesc("删除成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("删除异常");
        }
        return messageResp;
    }

    /**
     * 查询
     *
     * @return
     */
    @ApiOperation(value = "超时逗留查询", notes = "")
    @GetMapping
    public MessageResp selectRegionMonitorParking(@ModelAttribute RegionMonitorParking regionMonitorParking) {

        MessageResp messageResp = new MessageResp();
        PageInfo<RegionMonitorParking> regionMonitorParkingPageInfo = null;
        try {
            //create_time字段已删除
            if (null != regionMonitorParking.getOrderBy() && regionMonitorParking.getOrderBy().contains("time_desc")) {
                regionMonitorParking.setOrderBy("update_time_desc");
            }
            regionMonitorParkingPageInfo = regionMonitorParkingService.selectRegionMonitorParking(regionMonitorParking);
            messageResp.setData(regionMonitorParkingPageInfo.getList());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("查询异常");
        }
        return messageResp;
    }


    /**
     * 修改
     * @param regionMonitorParking
     * @return
     */
    @SystemControllerLog(description = "超时逗留修改")
    @ApiOperation(value = "超时逗留修改", notes = "")
    @PutMapping
    public MessageResp updateRegionMonitorParking(@RequestBody RegionMonitorParking regionMonitorParking) {
        MessageResp messageResp = new MessageResp();
        try {
            regionMonitorParkingService.updateRegionMonitorParking(regionMonitorParking);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("修改异常");
        }
        return messageResp;
    }


    @ApiOperation(value = "超时逗留车辆统计（所有围栏数据）", notes = "")
    @GetMapping("/statisticsList")
    public MessageResp statisticsParkings(@RequestParam Integer vehicleType) {
        MessageResp messageResp = new MessageResp();
        List<RegionAlarmInfo> regionAlarmInfoList = new ArrayList<>();
        try {
            //获取所有围栏
            List<MonitorResult> monitorResultList = Lists.newArrayList();
            List<RegionMonitorParking> regionMonitorParkingList = regionMonitorParkingService.selectAll();
            int alarmCount = 0;
            for (RegionMonitorParking regionMonitorParking : regionMonitorParkingList) {
                String key = null;
                //基于请求所需的数据类型，获取最新的数据（1-出租车，2-网约车）
                if (vehicleType == 2){
                    key = MonitorRedisConst.NETCAR_PARKING_RESULT_KEY + regionMonitorParking.getUuid();
                }
                if (vehicleType == 1){
                    key = MonitorRedisConst.TAXI_PARKING_RESULT_KEY + regionMonitorParking.getUuid();
                }
                String value = redisTemplate.opsForValue().get(key);
                MonitorResult monitorResult = JSON.parseObject(value, MonitorResult.class);
                //数据修饰，添加围栏名称，ID等信息
                if (null != monitorResult) {
                    monitorResultList.add(monitorResult);
                    if (monitorResult.getAlarmStatus() > -1) {
                        alarmCount++;
                    }
                    RegionAlarmInfo regionAlarmInfo = new RegionAlarmInfo();
                    regionAlarmInfo.setAlarmStatus(monitorResult.getAlarmStatus());
                    regionAlarmInfo.setRegionId(monitorResult.getRegionId());
                    regionAlarmInfo.setRegionName(regionMonitorParking.getRegionName());
                    regionAlarmInfoList.add(regionAlarmInfo);
                } else {
                    RegionAlarmInfo regionAlarmInfo = new RegionAlarmInfo();
                    regionAlarmInfo.setAlarmStatus(null);
                    regionAlarmInfo.setRegionId(regionMonitorParking.getUuid());
                    regionAlarmInfo.setRegionName(regionMonitorParking.getRegionName());
                    regionAlarmInfoList.add(regionAlarmInfo);
                }
            }

            messageResp.setData(regionAlarmInfoList);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("查询异常");
        }
        return messageResp;
    }

    @ApiOperation(value = "超时逗留车辆统计（所有围栏统计，多个围栏详细数据）", notes = "")
    @GetMapping("/statistics")
    public MessageResp statisticsParkingList(@RequestParam String regionIds, @RequestParam Integer vehicleType) {

        MessageResp messageResp = new MessageResp();
        RegionMonitor regionMonitor = new RegionMonitor();
        MonitorResult monitorResult = new MonitorResult();
        if (StringUtils.isBlank(regionIds)) {
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("电子围栏ID不能为空");
            return messageResp;
        }
        try {
            List<String> regionIdList = Splitter.on(",").splitToList(regionIds);
            List<MonitorResult> monitorResultList = new ArrayList();
            List<RegionMonitorParking> regionMonitorParkingList = regionMonitorParkingService.selectAll();
            int alarmCount = 0;
            //循坏所有围栏
            for (RegionMonitorParking regionMonitorParking : regionMonitorParkingList) {
                if (regionIds.contains(String.valueOf(regionMonitorParking.getUuid()))) {
                    String key = null;
                    //基于请求所需的数据类型，获取最新数据（1-出租车，2-网约车）
                    if (vehicleType == 2){
                        key = MonitorRedisConst.NETCAR_PARKING_RESULT_KEY + regionMonitorParking.getUuid();
                    }
                    if (vehicleType == 1){
                        key = MonitorRedisConst.TAXI_PARKING_RESULT_KEY + regionMonitorParking.getUuid();
                    }
                    String value = redisTemplate.opsForValue().get(key);
//                    System.out.println("(所有)测试数据展示  " + key + "：          " + value);
                    monitorResult = JSON.parseObject(value, MonitorResult.class);
                    //数据修饰，添加围栏名称，ID等信息
                    if (null != monitorResult) {
                        if (monitorResult.getAlarmStatus() > -1) {
                            alarmCount++;
                        }
                        //去掉多余的数据
                        monitorResult.setRegionViolationsList(null);
                        monitorResult.setGeofenceEntersList(null);
                        monitorResult.setFenceVehicleInfoList(null);
                        monitorResult.setRegionViolationTaxisList(null);
                        monitorResult.setRegionName(regionMonitorParking.getRegionName());
                        monitorResultList.add(monitorResult);
                    } else {
                        monitorResult = new MonitorResult();
                        monitorResult.setRegionId(regionMonitorParking.getUuid());
                        monitorResult.setRegionName(regionMonitorParking.getRegionName());
                        monitorResultList.add(monitorResult);
                    }
                }
            }
            regionMonitor.setTotalCount(regionMonitorParkingList.size());
            regionMonitor.setCurrentCount(regionIdList.size());
            regionMonitor.setAlarmCount(alarmCount);
            regionMonitor.setNormalCount(regionMonitorParkingList.size() - alarmCount);
            regionMonitor.setMonitorResultList(monitorResultList);

            messageResp.setData(regionMonitor);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("查询异常");
        }
        return messageResp;
    }
}
