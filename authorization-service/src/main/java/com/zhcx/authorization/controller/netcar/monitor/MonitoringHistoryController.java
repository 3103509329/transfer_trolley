package com.zhcx.authorization.controller.netcar.monitor;

import com.github.pagehelper.PageInfo;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.netcarbasic.facade.netcar.CompanyServiceService;
import com.zhcx.regionmonitor.monitor.MonitoringHistoryService;
import com.zhcx.regionmonitor.pojo.FenceResult;
import com.zhcx.regionmonitor.pojo.FenceVehicleInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/1/4 0004 11:30
 **/
@RestController
@RequestMapping("/monitor/history")
@Api(value = "区域监控异常历史记录")
public class MonitoringHistoryController {

    private Logger log = LoggerFactory.getLogger(MonitoringHistoryController.class);

    @Autowired
    private MonitoringHistoryService monitoringHistoryService;

    @Autowired
    private CompanyServiceService companyServiceService;

    @ApiOperation(value = "历史查询接口", notes = "")
    @GetMapping("/details")
    public MessageResp selectMonitoringHistory(HttpServletRequest request,
                                               @RequestParam(required = false)Integer fenceType,
                                               @RequestParam(required = false)Integer regionId,
                                               @RequestParam(required = false)Integer alarmType,
                                               @RequestParam(defaultValue = "1") Integer pageNo,
                                               @RequestParam(defaultValue = "10") Integer pageSize,
                                               @RequestParam String orderBy,
                                               @RequestParam String startTime,
                                               @RequestParam String endTime,
                                               @RequestParam Integer vehicleType){

        MessageResp messageResp = new MessageResp();
        PageInfo<FenceResult> fenceResult = null;
        try{
            fenceResult = monitoringHistoryService.selectMonitoringHistory(fenceType, regionId, alarmType, pageNo, pageSize, orderBy, startTime, endTime, vehicleType);
            if (fenceType == 0){
                fenceResult.getList().forEach(fence -> {
                    fence.setCompanyName(companyServiceService.selectCompanyNameByRegionId(fence.getRegionId()));
                });
            }
            messageResp.setData(fenceResult.getList());
            messageResp.setPageBean(PageBeanUtil.createPageBean(fenceResult));
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

    @ApiOperation(value = "查询车辆详情", notes = "")
    @GetMapping("/vehicles")
    public MessageResp queryVehicleList(HttpServletRequest request,
                                               @RequestParam(required = false)Integer fenceType,
                                               @RequestParam(required = false)Long fencingId,
                                               @RequestParam(defaultValue = "1") Integer pageNo,
                                               @RequestParam(defaultValue = "10") Integer pageSize,
                                               @RequestParam String orderBy){

        MessageResp messageResp = new MessageResp();
        try{
            PageInfo<FenceVehicleInfo> pageInfo = monitoringHistoryService.queryVehicleList(fenceType, fencingId, pageNo, pageSize, orderBy);
            messageResp.setData(pageInfo.getList());
            messageResp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
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
     * 获取所有的围栏信息
     * @param type
     * @return
     */
    @ApiOperation(value = "获取所有的围栏信息", notes = "")
    @GetMapping("/regions")
    public MessageResp selectFencs(@RequestParam Integer type, @RequestParam Integer vehicleType){
        MessageResp messageResp = new MessageResp();
        try{
            List<Map<String , Object>> result = monitoringHistoryService.selectFencs(type, vehicleType);
            messageResp.setData(result);
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
