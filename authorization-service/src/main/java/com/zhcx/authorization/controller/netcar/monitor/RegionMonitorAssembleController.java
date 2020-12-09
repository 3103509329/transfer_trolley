package com.zhcx.authorization.controller.netcar.monitor;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.config.SystemControllerLog;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.regionmonitor.constant.MonitorRedisConst;
import com.zhcx.regionmonitor.monitor.RegionMonitorAssembleService;
import com.zhcx.regionmonitor.pojo.RegionMonitorAssemble;
import com.zhcx.regionmonitor.response.RegionAlarmInfo;
import com.zhcx.regionmonitor.response.RegionMonitor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2018/12/5 0005 14:28
 **/
@RestController
@RequestMapping("/monitor/assemble")
@Api(value = "电子围栏区域监控" ,tags = "电子围栏区域监控（电子围栏信息）")
public class RegionMonitorAssembleController {

    private Logger log = LoggerFactory.getLogger(RegionMonitorAssembleController.class);

    @Autowired
    private RegionMonitorAssembleService regionMonitorAssembleService;

    /**
     * 电子围栏区域监控新增
     * @return
     */
    @SystemControllerLog(description = "电子围栏区域监控新增")
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
    public MessageResp selectRegionMonitorAssemble(@RequestParam(required = true) Integer fenceType,
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
    @SystemControllerLog(description = "电子围栏区域监控修改")
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
    @SystemControllerLog(description = "电子围栏区域监控删除")
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
    public MessageResp queryFenceRealTimeInfo(@RequestParam("fenceType") Integer fenceType,@RequestParam("regionIds") String regionIds,
                                              @RequestParam(required = false, defaultValue ="1") Integer vehicleType){
        MessageResp messageResp = new MessageResp();
        try {
            if(StringUtils.isBlank(regionIds)){
                return messageResp;
            }
            RegionMonitor regionMonitor = regionMonitorAssembleService.queryFenceRealTimeTile38(fenceType, regionIds, vehicleType);
            messageResp.setData(regionMonitor);
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

    private String getRedisKey(@PathVariable Integer fenceType, Long regionId) {
        String key = "";
        switch (fenceType){
            case 1:
                key = MonitorRedisConst.ASSEMBLE_RESULT_KEY + regionId;
                break;
            case 3:
                key = MonitorRedisConst.KEY_RESULT_KEY + regionId;
                break;
            case 4:
                key = MonitorRedisConst.CAPACITY_RESULT_KEY + regionId;
                break;
                default:
        }
        return key;
    }
}
