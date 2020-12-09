package com.zhcx.authorization.controller.taxi;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.*;
import com.zhcx.basicdata.facade.taxi.TaxiDispatchTaskService;
import com.zhcx.basicdata.pojo.taxi.TaxiDispatchTask;
import com.zhcx.common.util.UUIDUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @program: authorization-service
 * @ClassName:TaxiDispatchTaskController
 * @description: 调度任务控制类
 * @author: ZhangKai
 * @create: 2018-12-19 19:55
 **/
@RestController
@RequestMapping("/taxi/dispatchTask")
@Api(value = "dispatchPlan")
public class TaxiDispatchTaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaxiDispatchTaskController.class);

    @Autowired
    private UUIDUtils uuidUtils;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;


    @Autowired
    private TaxiDispatchTaskService taxiDispatchTaskService;

    @GetMapping("/")
    @ApiOperation(value = "查询出租车辆调度任务信息", notes = "参数为出租车辆调度任务信息对象")
    public MessageResp getRecord(HttpServletRequest request, @ModelAttribute TaxiDispatchTask param){
        MessageResp resp = new MessageResp();
        PageInfo pageInfo = null;
        try{
            pageInfo = taxiDispatchTaskService.selectByParam(param);
            if(pageInfo != null){
                resp.setData(pageInfo.getList());
                resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            }
            resp.setResultDesc("查询成功");
        }catch (Exception e){
            logger.error("查询异常,{}",e.getMessage());
            return CommonUtils.returnErrorInfo("查询异常");
        }
        return resp;
    }


    @PutMapping("/stopTask/{uuid}")
    public MessageResp stopTask(@PathVariable(value = "uuid") String uuid){
        MessageResp resp = new MessageResp();
        try{
            taxiDispatchTaskService.stopTask(Long.parseLong(uuid));
            resp.setResultDesc("终止成功");
        }catch (Exception e){
            logger.error("终止异常,{}",e.getMessage());
            return CommonUtils.returnErrorInfo("终止失败");
        }
        return resp;
    }

    @DeleteMapping("/deleteTask/{uuid}")
    @ApiOperation(value = "删除出租调度任务信息", notes = "参数UUID")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", dataType = "String", name = "uuid", value = "任务UUID", required = true)})
    public MessageResp deleteRecord(@PathVariable(value = "uuid") String uuid){
        MessageResp resp = new MessageResp();
        int delRes = 0;
        try{
            delRes = taxiDispatchTaskService.deleRecord(Long.parseLong(uuid));
            resp.setData(delRes);
            resp.setResultDesc("删除成功");
        }catch (Exception e){
            logger.error("删除异常,{}",e.getMessage());
            return CommonUtils.returnErrorInfo("删除失败");
        }
        return resp;
    }


    @PostMapping("/updateTask")
    @ApiOperation(value = "更新出租车辆调度任务信息", notes = "参数为出租车辆调度任务信息对象")
    public MessageResp updateRecord(HttpServletRequest request, @RequestBody TaxiDispatchTask record){
        MessageResp resp = new MessageResp();
        int updateRes = 0;
        try {
            updateRes = taxiDispatchTaskService.updateRecord(record);
            resp.setData(updateRes);
            resp.setResultDesc("更新成功");
        }catch (Exception e){
            logger.error("更新异常,{}",e.getMessage());
            return CommonUtils.returnErrorInfo("更新失败");
        }
        return resp;
    }

    @PostMapping("/addTask")
    @ApiOperation(value = "添加出租车辆调度任务信息", notes = "参数为出租车辆调度任务信息对象")
    public MessageResp addRecord(HttpServletRequest request, @RequestBody TaxiDispatchTask record){
        MessageResp resp = new MessageResp();
        int addRes = 0;
        AuthUserResp authUser = sessionHandler.getUser(request);
        record.setCreateBy(authUser.getUserName());
        try {
            Long uuid = uuidUtils.getLongUUID();
            record.setUuid(uuid);
            addRes = taxiDispatchTaskService.addRecord(record);
            //如果新增成功,将其添加到定时任务中
            if(addRes > 0){
                resp.setData(addRes);
                resp.setResultDesc("添加成功");
            }
        }catch (Exception e){
            logger.error("添加异常,{}",e.getMessage());
            return CommonUtils.returnErrorInfo("新增调度任务失败");
        }
        return resp;
    }

    @GetMapping("/getEfficienty/{taskId}")
    @ApiOperation(value = "查询调度任务效率信息", notes = "调度任务ID")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", dataType = "String", name = "taskId", value = "调度任务ID", required = true)})
    public MessageResp getEfficienty(@PathVariable(value = "taskId") String taskId){
        MessageResp result = new MessageResp();
        Map<String,Object> efficientyRes = null;
        try{
            efficientyRes = taxiDispatchTaskService.getEfficienty(taskId);
            result.setResultDesc("查询成功");
            result.setData(efficientyRes);
        }catch (Exception e){
            logger.error("查询效率信息异常,{}",e.getMessage());
            return CommonUtils.returnErrorInfo("查询异常");
        }
        return result;
    }


}
