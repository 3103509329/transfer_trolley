package com.zhcx.authorization.controller.netcar.monitor;

import com.zhcx.authorization.config.SystemControllerLog;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.regionmonitor.pojo.AssembleRules;
import com.zhcx.regionmonitor.pojo.MonitorAssembleRule;
import com.zhcx.regionmonitor.monitor.MonitorAssembleRuleService;
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
@RequestMapping("/monitor/assemblerule")
@Api(value = "assemblerule",tags = "异常聚集区域监控规则")
public class MonitorAssembleRuleController {

    private Logger log = LoggerFactory.getLogger(MonitorAssembleRuleController.class);

    @Autowired
    private MonitorAssembleRuleService monitorAssembleRuleService;

    /**
     * 新增 or 修改
     * @param
     * @return
     */
    @SystemControllerLog(description = "异常聚集区域监控规则新增/修改")
    @ApiOperation(value = "异常聚集区域监控规则新增 or 修改", notes = "")
    @PostMapping
    public MessageResp addMonitorAssembleRule (@RequestBody AssembleRules assembleRules){

        MessageResp messageResp = new MessageResp();
        try{
            List<MonitorAssembleRule> monitorAssembleRuleList =  monitorAssembleRuleService.addMonitorAssembleRule(assembleRules);
            messageResp.setData(monitorAssembleRuleList);
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
     * @param regionId
     * @return
     */
    @ApiOperation(value = "异常聚集区域监控规则查询", notes = "")
    @GetMapping
    public MessageResp selectMonitorAssembleRule(@RequestParam Long regionId){
        MessageResp messageResp = new MessageResp();
        try{
            List<MonitorAssembleRule> monitorAssembleRuleList = monitorAssembleRuleService.selectMonitorAssembleRule(regionId);
            messageResp.setData(monitorAssembleRuleList);
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
