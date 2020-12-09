package com.zhcx.authorization.controller.netcar.monitor;

import com.zhcx.authorization.config.SystemControllerLog;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.regionmonitor.monitor.MonitorCapacityRuleService;
import com.zhcx.regionmonitor.params.CapacityRuleList;
import com.zhcx.regionmonitor.pojo.MonitorCapacityRule;
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
 * @date 2018/12/5 0005 19:36
 **/
@RestController
@RequestMapping("/monitor/capacityrule")
@Api(value = "运力监控规则",tags = "运力监控规则")
public class MonitorCapacityRuleController {

    private Logger log = LoggerFactory.getLogger(MonitorCapacityRuleController.class);

    @Autowired
    private MonitorCapacityRuleService monitorCapacityRuleService;

    /**
     * 新增 or 修改
     * @param
     * @return
     */
    @SystemControllerLog(description = "运力监控规则新增/修改")
    @ApiOperation(value = "运力监控规则新增 or 修改", notes = "")
    @PostMapping
    public MessageResp addMonitorCapacityRule (@RequestBody CapacityRuleList capacityRuleList){

        MessageResp messageResp = new MessageResp();
        try{
            List<MonitorCapacityRule> ruleList =  monitorCapacityRuleService.addMonitorCapacityRule(capacityRuleList);
            messageResp.setData(ruleList);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("新增成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("新增异常");
        }
        return messageResp;
    }

    /**
     * 查询
     * @param
     * @return
     */
    @ApiOperation(value = "运力监控规则查询", notes = "")
    @GetMapping("/{regionId}")
    public MessageResp selectMonitorCapacityRule(@PathVariable Long regionId){
        MessageResp messageResp = new MessageResp();
        try{
            List<MonitorCapacityRule> monitorCapacityRuleList = monitorCapacityRuleService.selectMonitorCapacityRule(regionId);
            messageResp.setData(monitorCapacityRuleList);
            messageResp.setResult(Boolean.TRUE.toString());
            messageResp.setResultDesc("查询成功");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            messageResp.setStatusCode("-50");
            messageResp.setResult(Boolean.FALSE.toString());
            messageResp.setResultDesc("查询异常");
        }
        return messageResp;
    }

}
