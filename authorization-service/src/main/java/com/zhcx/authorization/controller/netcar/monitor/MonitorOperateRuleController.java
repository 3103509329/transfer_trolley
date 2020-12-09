package com.zhcx.authorization.controller.netcar.monitor;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.config.SystemControllerLog;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.regionmonitor.monitor.MonitorOperateRuleService;
import com.zhcx.regionmonitor.pojo.MonitorOperateRule;
import com.zhcx.regionmonitor.pojo.OperateRules;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2018/12/5 0005 20:45
 **/
@RestController
@RequestMapping("/monitor/operaterule")
@Api(value = "operaterule",tags = "超区域经营规则")
public class MonitorOperateRuleController {

    private Logger log = LoggerFactory.getLogger(MonitorOperateRuleController.class);

    @Autowired
    private MonitorOperateRuleService monitorOperateRuleService;


    /**
     * 新增 or 修改
     * @param
     * @return
     */
    @SystemControllerLog(description = "超区域经营规则新增")
    @ApiOperation(value = "超区域经营规则新增", notes = "")
    @PostMapping
    public MessageResp addMonitorOperateRule(@RequestBody OperateRules operateRules){
        MessageResp messageResp = new MessageResp();
        PageInfo<MonitorOperateRule> monitorOperateRulePageInfo = null;
        try{
            monitorOperateRulePageInfo = monitorOperateRuleService.addMonitorOperateRule(operateRules);
            messageResp.setData(monitorOperateRulePageInfo.getList());
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
    @SystemControllerLog(description = "超区域经营规则删除")
    @ApiOperation(value = "超区域经营规则删除", notes = "")
    @DeleteMapping
    public MessageResp deleteMonitorOperateRule(@RequestBody MonitorOperateRule monitorOperateRule){
        MessageResp messageResp = new MessageResp();
        try{
            monitorOperateRuleService.deleteMonitorOperateRule(monitorOperateRule);
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
     * @param monitorOperateRule
     * @return
     */
    @ApiOperation(value = "超区域经营规则查询", notes = "")
    @GetMapping
    public MessageResp selectMonitorOperateRule(@ModelAttribute MonitorOperateRule monitorOperateRule) {
        MessageResp messageResp = new MessageResp();
        PageInfo<MonitorOperateRule> monitorOperateRulePageInfo = null;
        try {
            monitorOperateRulePageInfo = monitorOperateRuleService.selectMonitorOperateRule(monitorOperateRule);
            messageResp.setData(monitorOperateRulePageInfo.getList());
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
     * @param monitorOperateRule
     * @return
     */
    @SystemControllerLog(description = "超区域经营规则修改")
    @ApiOperation(value = "超区域经营规则修改", notes = "")
    @PutMapping
    public MessageResp updateMonitorOperateRule(@RequestBody MonitorOperateRule monitorOperateRule){
        MessageResp messageResp = new MessageResp();
        try{
            monitorOperateRuleService.updateMonitorOperateRule(monitorOperateRule);
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
}
