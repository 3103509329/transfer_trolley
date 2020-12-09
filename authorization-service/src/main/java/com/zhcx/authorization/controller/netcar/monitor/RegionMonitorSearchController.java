package com.zhcx.authorization.controller.netcar.monitor;

import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.regionmonitor.monitor.RegionMonitorSearchService;
import com.zhcx.regionmonitor.params.RegionMonitorOrder;
import com.zhcx.regionmonitor.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/monitor/search")
@Api(value = "查车区域监控" ,tags = "查车区域监控（电子围栏信息）")
public class RegionMonitorSearchController {

    private Logger log = LoggerFactory.getLogger(RegionMonitorSearchController.class);

    @Autowired
    private RegionMonitorSearchService regionMonitorSearchService;

    /**
      * 查车区域监控
      * @return
      */
    @ApiOperation(value = "查车区域监控", notes = "查车区域监控")
    @PostMapping
    public MessageResp regionSearch(@RequestBody RegionSearch regionMonitorSearchList){


        MessageResp messageResp = new MessageResp();
        try{
            List<RegionVehicle> regionVehicleList = regionMonitorSearchService.regionSearch(regionMonitorSearchList);
            messageResp.setData(regionVehicleList);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }



    /**
     * 运力特征
     * @param regionMonitorOrder
     * @return
     */
    @PostMapping("/order")
    public MessageResp queryRegionOrder(@RequestBody RegionMonitorOrder regionMonitorOrder){
        MessageResp messageResp = new MessageResp();
        try{
            List list = regionMonitorSearchService.queryRegionOrder(regionMonitorOrder);
            messageResp.setData(list);
            messageResp.setResultDesc("查询成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonUtils.returnErrorInfo("查询失败");
        }
        return messageResp;
    }
}
