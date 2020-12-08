package com.zhcx.netcar.netcarservice.controller.app;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zhcx.netcar.constant.HandleStatusEnum;
import com.zhcx.netcar.netcarservice.utils.CommonUtils;
import com.zhcx.netcar.netcarservice.utils.MessageResp;
import com.zhcx.netcar.netcarservice.utils.PageBeanUtil;
import com.zhcx.regionmonitor.constant.MonitorRedisConst;
import com.zhcx.regionmonitor.dubbo.AssembleHandleDubboService;
import com.zhcx.regionmonitor.dubbo.RegionMonitorAssembleDubboService;
import com.zhcx.regionmonitor.monitor.AssembleHandleService;
import com.zhcx.regionmonitor.monitor.RegionMonitorAssembleService;
import com.zhcx.regionmonitor.pojo.AssembleHandle;
import com.zhcx.regionmonitor.pojo.NetcarAppAssembleHandle;
import com.zhcx.regionmonitor.pojo.RegionMonitorAssemble;
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
import java.util.List;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2018/12/5 0005 14:28
 **/
@RestController
@RequestMapping("/netcar/app/monitor/assemble")
@Api(value = "电子围栏区域监控" ,tags = "电子围栏区域监控（电子围栏信息）")
public class RegionMonitorAssembleController {

    private Logger log = LoggerFactory.getLogger(RegionMonitorAssembleController.class);

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private RegionMonitorAssembleDubboService regionMonitorAssembleService;

    @Reference(version = "${zhcx.business.dubbo.version}", check = false)
    private AssembleHandleDubboService assembleHandleService;

    @Resource(name="redisTemplate4Json")
    private RedisTemplate<String, String> redisTemplate;
    /**
     * 电子围栏区域监控新增
     * @return
     */
    @ApiOperation(value = "电子围栏区域监控新增", notes = "电子围栏区域监控新增")
    @PostMapping
    public MessageResp addRegionMonitorAssemble(@RequestBody RegionMonitorAssemble regionMonitorAssemble){

        MessageResp messageResp = new MessageResp();
        try{
            RegionMonitorAssemble result = regionMonitorAssembleService.addRegionMonitorAssemble(regionMonitorAssemble);
            messageResp.setData(result);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("新增成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("新增失败");
        }
        return messageResp;
    }

    /**
     * 查询
     *
     * @param vehicleType 监控车辆类型 1-出租车 2-网约车
     * @return
     */
    @ApiOperation(value = "电子围栏区域监控查询", notes = "电子围栏区域监控查询")
    @GetMapping
    public MessageResp selectRegionMonitorAssemble(@RequestParam Integer fenceType,
                                                   @RequestParam(required = false, defaultValue ="1") Integer vehicleType,
                                                   @RequestParam(required = false) Integer pageNo,
                                                   @RequestParam(required = false) Integer pageSize,
                                                   @RequestParam(required = false) String orderBy) {

        MessageResp messageResp = new MessageResp();
        try {
            //create_time字段已删除
            if(null != orderBy && orderBy.contains("time_desc") ){
                orderBy = ("update_time_desc");
            }
            PageInfo<RegionMonitorAssemble> pageInfo = regionMonitorAssembleService.selectRegionMonitorAssemble(fenceType, pageNo, pageSize, orderBy, vehicleType);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    /**
     * 修改
     * @param regionMonitorAssemble
     * @return
     */
    @ApiOperation(value = "电子围栏区域监控修改", notes = "电子围栏区域监控修改")
    @PutMapping
    public MessageResp updateRegionMonitorAssemble(@RequestBody RegionMonitorAssemble regionMonitorAssemble){

        MessageResp messageResp = new MessageResp();
        try{
            int row = regionMonitorAssembleService.updateRegionMonitorAssemble(regionMonitorAssemble);
            if (row == 0) {
                return CommonUtils.returnErrorInfo("修改失败");
            }
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("修改成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("修改失败");
        }
        return messageResp;
    }

    /**
     * 删除
     * @param
     * @return
     */
    @ApiOperation(value = "电子围栏区域监控删除", notes = "电子围栏区域监控删除")
    @DeleteMapping("/{uuid}")
    public MessageResp deleteRegionMonitorAssemble(@PathVariable Long uuid){
        MessageResp messageResp = new MessageResp();
        try{
            int i = regionMonitorAssembleService.deleteRegionMonitorAssemble(uuid);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("删除成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("删除失败");
        }
        return messageResp;
    }

    /**
     * 查询电子围栏区域内实时信息
     * 根据区域ID从redis获取最新数据
     * @param
     * @return
     */
    @ApiOperation(value = "查询指定电子围栏区域内实时信息", notes = "查询指定聚集区域内实时信息")
    @GetMapping("/realTime")
    public MessageResp queryFenceRealTimeInfo(@RequestParam("fenceType") Integer fenceType, @RequestParam("regionIds") String regionIds,
                                              @RequestParam(required = false, defaultValue ="1") Integer vehicleType){
        MessageResp messageResp = new MessageResp();
        try {
            if(StringUtils.isBlank(regionIds)){
                return messageResp;
            }
            RegionMonitor regionMonitor = regionMonitorAssembleService.queryFenceRealTimeTile38(fenceType, regionIds, vehicleType);
            messageResp.setResultDesc("查询成功");
            messageResp.setData(regionMonitor.getMonitorResultList());
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;

    }

    /**
     * 查询所有电子围栏区域报警信息
     * @param
     * @return
     */
    @ApiOperation(value = "查询所有电子围栏区域报警信息", notes = "查询所有电子围栏区域报警信息")
    @GetMapping("/regionAlarmInfo/{fenceType}")
    public MessageResp regionAlarmInfo(@PathVariable("fenceType")Integer fenceType,
                                       @RequestParam(required = false, defaultValue ="1") Integer vehicleType){
        MessageResp messageResp = new MessageResp();
        try{
            List<RegionAlarmInfo> regionAlarmInfoList = regionMonitorAssembleService.queryFenceAlarmTile38(fenceType, vehicleType);
            messageResp.setData(regionAlarmInfoList);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("请求成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }


    /**
     * APP查询电子围栏区域报警处理信息
     * @param
     * @return
     */
    @ApiOperation(value = "APP查询电子围栏区域报警处理信息", notes = "APP查询电子围栏区域报警处理信息")
    @GetMapping("/handle")
    public MessageResp queryAssembleHandleList(@RequestParam("regionIds") String regionIds,
                                               @RequestParam(required = false, defaultValue ="1") Integer vehicleType){
        MessageResp messageResp = new MessageResp();
        if(StringUtils.isBlank(regionIds)){
            return messageResp;
        }
        List<AssembleHandle> list = Lists.newArrayList();
        String[] fenceIds= StringUtils.split(regionIds, ",");
        for (String fenceId : fenceIds) {
            String appRedisKey = MonitorRedisConst.APP_ASSEMBLE_RESULT_KEY + fenceId;
            String appAssembleValue = redisTemplate.opsForValue().get(appRedisKey);
            if (StringUtils.isNotBlank(appAssembleValue)) {
                AssembleHandle assembleHandle = JSON.parseObject(appAssembleValue, AssembleHandle.class);
                list.add(assembleHandle);
            }
        }
        messageResp.setData(list);
        return messageResp;
    }

    /**
     * 围栏报警处理列表
     * @param
     * @return
     */
    @ApiOperation(value = "围栏报警处理列表", notes = "围栏报警处理列表")
    @GetMapping("/handle/{fenceId}")
    public MessageResp queryHandleList(@PathVariable("fenceId")Long fenceId,
                                       @RequestParam(required = false, defaultValue ="1") Integer vehicleType,
                                       @RequestParam(required = false) Integer pageNo,
                                       @RequestParam(required = false) Integer pageSize,
                                       @RequestParam(required = false,defaultValue = "create_time_desc") String orderBy){
        MessageResp messageResp = new MessageResp();
        try {
            PageInfo<NetcarAppAssembleHandle> pageInfo = assembleHandleService.queryHandleList(fenceId, pageNo, pageSize, orderBy);
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            messageResp.setData(pageInfo.getList());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }

    /**
     * 围栏报警处理
     * @param
     * @return
     */
    @ApiOperation(value = "围栏报警处理", notes = "围栏报警处理")
    @PostMapping("/handle")
    public MessageResp insertAssembleHandle(@RequestBody NetcarAppAssembleHandle netcarAppAssembleHandle){
        MessageResp messageResp = new MessageResp();
        try {
            Long fenceId = netcarAppAssembleHandle.getFenceId();
            if(null == fenceId){
                return CommonUtils.returnErrorInfo("参数不正确");
            }
            int row =  assembleHandleService.insertAssembleHandle(netcarAppAssembleHandle);
            if (row > 0) {
                String appRedisKey = MonitorRedisConst.APP_ASSEMBLE_RESULT_KEY + netcarAppAssembleHandle.getFenceId();
                String appAssembleValue = redisTemplate.opsForValue().get(appRedisKey);
                log.error("appRedisKey:{}, appAssembleValue:{}",appRedisKey, appAssembleValue);
                if (StringUtils.isNotBlank(appAssembleValue)) {
                    AssembleHandle assembleHandle = JSON.parseObject(appAssembleValue, AssembleHandle.class);
                    //修改状态 为处理中
                    log.error("TIME1:{}, TIME2:{}",assembleHandle.getHandleTime().getTime(),netcarAppAssembleHandle.getHandleTime().getTime());
                    assembleHandle.setHandleStatus(HandleStatusEnum.HANDLING.getCode());
                    //处理后持续时间清零
                    assembleHandle.setDuration(0L);
                    redisTemplate.opsForValue().set(appRedisKey, JSON.toJSONString(assembleHandle));
                    log.error("appRedisKey2:{}, appAssembleValue2:{}",appRedisKey, appAssembleValue);
//                    if(assembleHandle.getHandleTime().getTime() == netcarAppAssembleHandle.getHandleTime().getTime()){
//                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("处理失败");
        }
        return messageResp;
    }

}
