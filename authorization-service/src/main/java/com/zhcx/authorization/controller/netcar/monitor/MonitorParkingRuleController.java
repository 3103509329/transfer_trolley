package com.zhcx.authorization.controller.netcar.monitor;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.config.SystemControllerLog;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.regionmonitor.monitor.MonitorParkingRuleService;
import com.zhcx.regionmonitor.pojo.MonitorParkingRule;
import com.zhcx.regionmonitor.pojo.ParkingRules;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2018/12/5 0005 21:08
 **/
@RestController
@RequestMapping("/monitor/parkingrule")
@Api(value = "parkingrule",tags = "超时停车规则")
public class MonitorParkingRuleController {

    private Logger log = LoggerFactory.getLogger(MonitorParkingRuleController.class);

    @Autowired
    private MonitorParkingRuleService monitorParkingRuleService;


    /**
     * 新增 or 修改
     * @param
     * @return
     */
    @SystemControllerLog(description = "超时停车规则新增/修改")
    @ApiOperation(value = "超时停车规则新增", notes = "")
    @PostMapping
    public MessageResp addMonitorParkingRule(@RequestBody ParkingRules parkingRules){

//        System.out.println(parkingRules.toString());
        MessageResp messageResp = new MessageResp();
         PageInfo<MonitorParkingRule> monitorParkingRulePageInfo = null;
        try{
            monitorParkingRulePageInfo = monitorParkingRuleService.addMonitorParkingRule(parkingRules);
            messageResp.setData(monitorParkingRulePageInfo.getList());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("新增成功");
        }catch (Exception e){
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
    @SystemControllerLog(description = "超时停车规则删除")
    @ApiOperation(value = "超时停车规则删除", notes = "")
    @DeleteMapping
    public MessageResp deleteMonitorParkingRule(@RequestBody MonitorParkingRule monitorParkingRule){
        MessageResp messageResp = new MessageResp();
        try{
            monitorParkingRuleService.deleteMonitorParkingRule(monitorParkingRule);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("删除成功");
        }catch (Exception e){
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
     * @param monitorParkingRule
     * @return
     */
    @ApiOperation(value = "超时停车规则查询", notes = "")
    @GetMapping
    public MessageResp selectMonitorParkingRule(@ModelAttribute MonitorParkingRule monitorParkingRule){
        MessageResp messageResp = new MessageResp();
        PageInfo<MonitorParkingRule> monitorParkingRulePageInfo = null;
        try{
            monitorParkingRulePageInfo = monitorParkingRuleService.selectMonitorParkingRule(monitorParkingRule);
            messageResp.setData(monitorParkingRulePageInfo.getList());
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        }catch (Exception e){
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
     * @param monitorParkingRule
     * @return
     */
    @SystemControllerLog(description = "超时停车规则修改")
    @ApiOperation(value = "超时停车规则修改", notes = "")
    @PutMapping
    public MessageResp updateMonitorParkingRule(@RequestBody MonitorParkingRule monitorParkingRule){
        MessageResp messageResp = new MessageResp();
        try{
            monitorParkingRuleService.updateMonitorParkingRule(monitorParkingRule);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("修改成功");
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("修改异常");
        }
        return messageResp;
    }


}
