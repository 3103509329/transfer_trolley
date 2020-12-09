package com.zhcx.authorization.controller.netcar.monitor;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.zhcx.authorization.config.SystemControllerLog;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.regionmonitor.constant.MonitorRedisConst;
import com.zhcx.regionmonitor.monitor.MonitorCompanyService;
import com.zhcx.regionmonitor.monitor.RegionMonitorOperateService;
import com.zhcx.regionmonitor.pojo.NetcarBaseInfoCompany;
import com.zhcx.regionmonitor.pojo.RegionMonitorOperate;
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
 * @date 2018/12/5 0005 15:13
 **/
@RequestMapping("/monitor/operate")
@RestController
@Api(value = "operate", tags = "超区域经营（电子围栏信息）")
public class RegionMonitorOperateController {

    private Logger log = LoggerFactory.getLogger(RegionMonitorParkingController.class);

    @Autowired
    private RegionMonitorOperateService regionMonitorOperateService;

    @Autowired
    private MonitorCompanyService monitorCompanyService;

    @Resource(name = "redisTemplate4Json")
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 新增
     *
     * @param regionMonitorOperate
     * @return
     */
    @SystemControllerLog(description = "超区域经营信息新增")
    @ApiOperation(value = "超区域经营信息新增", notes = "")
    @PostMapping
    public MessageResp addRegionMonitorOperate(@RequestBody RegionMonitorOperate regionMonitorOperate) {

        MessageResp messageResp = new MessageResp();
        try {
            RegionMonitorOperate regionMonitorOperatePageInfo = regionMonitorOperateService.addRegionMonitorOperate(regionMonitorOperate);
            if (null == regionMonitorOperatePageInfo) {
                messageResp.setStatusCode("-50");
                messageResp.setResult(Boolean.FALSE.toString());
                messageResp.setResultDesc("本公司电子围栏已存在");
            } else {
                messageResp.setData(regionMonitorOperatePageInfo);
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
     *
     * @param
     * @return
     */
    @SystemControllerLog(description = "超区域经营信息删除")
    @ApiOperation(value = "超区域经营信息删除", notes = "")
    @DeleteMapping("/{uuid}")
    public MessageResp deleteRegionMonitorOperate(@PathVariable Long uuid) {
        MessageResp messageResp = new MessageResp();
        try {
            int i = regionMonitorOperateService.deleteRegionMonitorOperate(uuid);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("删除成功");
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
     * 超区域经营公司（未注册）查询
     *
     * @return
     */
    @ApiOperation(value = "未注册公司信息获取（超区域经营）", notes = "")
    @GetMapping("/companys")
    public MessageResp selectCompanyId(@RequestParam Integer vehicleType) {
        MessageResp messageResp = new MessageResp();
        try {
            List<NetcarBaseInfoCompany> companyList = monitorCompanyService.unregisteredCompany(vehicleType);
            messageResp.setData(companyList);
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
     * 围栏信息查询
     *
     * @param regionMonitorOperate
     * @return
     */
    @ApiOperation(value = "超区域经营信息查询", notes = "")
    @GetMapping
    public MessageResp selectRegionMonitorOperate(@ModelAttribute RegionMonitorOperate regionMonitorOperate) {

        MessageResp messageResp = new MessageResp();
        PageInfo<RegionMonitorOperate> regionMonitorOperatePageInfo = null;
        try {
            //create_time字段已删除
            if (null != regionMonitorOperate.getOrderBy() && regionMonitorOperate.getOrderBy().contains("time_desc")) {
                regionMonitorOperate.setOrderBy("update_time_desc");
            }
            regionMonitorOperatePageInfo = regionMonitorOperateService.selectRegionMonitorOperate(regionMonitorOperate);
            messageResp.setData(regionMonitorOperatePageInfo.getList());
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
     *
     * @param regionMonitorOperate
     * @return
     */
    @SystemControllerLog(description = "超区域经营信息修改")
    @ApiOperation(value = "超区域经营信息修改", notes = "")
    @PutMapping
    public MessageResp updateRegionMonitorOperate(@RequestBody RegionMonitorOperate regionMonitorOperate) {

        MessageResp messageResp = new MessageResp();
        try {
            regionMonitorOperateService.updateRegionMonitorOperate(regionMonitorOperate);
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

    @ApiOperation(value = "超区域经营车辆统计", notes = "")
    @GetMapping("/statistics")
    public MessageResp statisticsParkings(@RequestParam String regionIds, @RequestParam Integer vehicleType) {
        MessageResp messageResp = new MessageResp();
        RegionMonitor regionMonitor = new RegionMonitor();
        List<String> regionIdList = new ArrayList<>();
        MonitorResult monitorResult = new MonitorResult();
        if (!StringUtils.isBlank(regionIds)) {
            regionIdList = Splitter.on(",").splitToList(regionIds);
        }
        try {
            List<MonitorResult> monitorResultList = Lists.newArrayList();
            List<RegionMonitorOperate> regionMonitorOperateList = regionMonitorOperateService.selectAll(vehicleType);
            int alarmCount = 0;
            for (RegionMonitorOperate regionMonitorOperate : regionMonitorOperateList) {
                if (regionIds.contains(String.valueOf(regionMonitorOperate.getUuid()))) {
                    String key = null;
                    //网约车
                    if (vehicleType == 2) {
                        key = MonitorRedisConst.NETCAR_OPERATE_RESULT_KEY + regionMonitorOperate.getUuid();
                    }
                    //出租车
                    if (vehicleType == 1) {
                        key = MonitorRedisConst.TAXI_OPERATE_RESULT_KEY + regionMonitorOperate.getUuid();
                    }
                    //所有
                    if (vehicleType == 0) {

                    }

                    String value = redisTemplate.opsForValue().get(key);
//                    System.out.println("测试数据展示：          " + value);
                    monitorResult = JSON.parseObject(value, MonitorResult.class);
                    //数据修饰，添加围栏名称，ID等信息
                    if (null != monitorResult) {
                        if (monitorResult.getAlarmStatus() > -1) {
                            alarmCount++;
                        }
                        monitorResult.setRegionViolationsList(null);
                        monitorResult.setGeofenceEntersList(null);
                        monitorResult.setFenceVehicleInfoList(null);
                        monitorResult.setRegionViolationTaxisList(null);
                        monitorResult.setRegionName(regionMonitorOperate.getRegionName());
                        monitorResultList.add(monitorResult);
                    } else {
                        monitorResult = new MonitorResult();
                        monitorResult.setRegionId(regionMonitorOperate.getUuid());
                        monitorResult.setRegionName(regionMonitorOperate.getRegionName());
                        monitorResultList.add(monitorResult);
                    }
                }
            }
            regionMonitor.setTotalCount(regionMonitorOperateList.size());
            regionMonitor.setCurrentCount(regionIdList.size());
            regionMonitor.setAlarmCount(alarmCount);
            regionMonitor.setNormalCount(regionMonitorOperateList.size() - alarmCount);
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


    @ApiOperation(value = "超区域经营车辆统计（所有围栏数据）", notes = "")
    @GetMapping("/statisticsList")
    public MessageResp statisticsParkingList(@RequestParam Integer vehicleType) {
        MessageResp messageResp = new MessageResp();
        List<RegionAlarmInfo> regionAlarmInfoList = new ArrayList<>();
        try {
            List<RegionMonitorOperate> regionMonitorOperateList = regionMonitorOperateService.selectAll(vehicleType);
            int alarmCount = 0;
            for (RegionMonitorOperate regionMonitorOperate : regionMonitorOperateList) {
                String key = null;
                //网约车
                if (vehicleType == 2) {
                    key = MonitorRedisConst.NETCAR_OPERATE_RESULT_KEY + regionMonitorOperate.getUuid();
                }
                //出租车
                if (vehicleType == 1) {
                    key = MonitorRedisConst.TAXI_OPERATE_RESULT_KEY + regionMonitorOperate.getUuid();
                }
                //所有
                if (vehicleType == 0) {

                }

                String value = redisTemplate.opsForValue().get(key);
//                System.out.println("测试数据展示：          " + value);
                MonitorResult monitorResult = JSON.parseObject(value, MonitorResult.class);
                //数据修饰，添加围栏名称，ID等信息
                if (null != monitorResult) {
                    if (monitorResult.getAlarmStatus() > -1) {
                        alarmCount++;
                    }
                    RegionAlarmInfo regionAlarmInfo = new RegionAlarmInfo();
                    regionAlarmInfo.setAlarmStatus(monitorResult.getAlarmStatus());
                    regionAlarmInfo.setRegionId(monitorResult.getRegionId());
                    regionAlarmInfo.setRegionName(regionMonitorOperate.getRegionName());
                    regionAlarmInfoList.add(regionAlarmInfo);
                } else {
                    RegionAlarmInfo regionAlarmInfo = new RegionAlarmInfo();
                    regionAlarmInfo.setAlarmStatus(null);
                    regionAlarmInfo.setRegionId(regionMonitorOperate.getUuid());
                    regionAlarmInfo.setRegionName(regionMonitorOperate.getRegionName());
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


}
