package com.zhcx.authorization.controller.netcar.monitor;

import com.zhcx.authorization.config.SystemControllerLog;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.regionmonitor.monitor.MonitorKeyRuleService;
import com.zhcx.regionmonitor.pojo.KeyRules;
import com.zhcx.regionmonitor.pojo.MonitorKeyRule;
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
 * @date 2018/12/5 0005 20:01
 **/
@RestController
@RequestMapping("/monitor/keyrule")
@Api(value = "keyrule",tags = "重点区域监控规则")
public class MonitorKeyRuleController {

    private Logger log = LoggerFactory.getLogger(MonitorKeyRuleController.class);

    @Autowired
    private MonitorKeyRuleService monitorKeyRuleService;


    /**
     * 新增 or 修改
     * @param
     * @return
     */
    @SystemControllerLog(description = "重点区域监控规则新增/修改")
    @ApiOperation(value = "重点区域监控规则新增", notes = "")
    @PostMapping
    public MessageResp addMonitorKeyRule(@RequestBody KeyRules keyRules){
        MessageResp messageResp = new MessageResp();
        try{
            List<MonitorKeyRule> monitorKeyRuleList = monitorKeyRuleService.addMonitorKeyRule(keyRules);
            messageResp.setData(monitorKeyRuleList);
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
    @ApiOperation(value = "重点区域监控规则查询", notes = "")
    @GetMapping
    public MessageResp selectMonitorKeyRule(@RequestParam Long regionId){
        MessageResp messageResp = new MessageResp();
        try{
            List<MonitorKeyRule> monitorKeyRuleList = monitorKeyRuleService.selectMonitorKeyRule(regionId);
            messageResp.setData(monitorKeyRuleList);
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
