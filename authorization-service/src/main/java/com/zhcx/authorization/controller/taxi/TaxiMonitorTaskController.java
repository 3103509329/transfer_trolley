package com.zhcx.authorization.controller.taxi;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.Constants;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.basicdata.facade.taxi.TaxiMonitorTaskService;
import com.zhcx.basicdata.pojo.taxi.TaxiMonitorTask;
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
import java.util.Date;
import java.util.List;

/**
 * @description: 视频监控任务类
 * @author: qiuziqiang
 * @date 2019-05-10 14:36
 **/
@RestController
@RequestMapping("/taxi/monitorTask")
@Api(value = "monitorTask")
public class TaxiMonitorTaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaxiMonitorTaskController.class);

    @Autowired
    private UUIDUtils uuidUtils;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @Autowired
    private TaxiMonitorTaskService taxiMonitorTaskService;

    @GetMapping("/queryTaskList")
    @ApiOperation(value = "查询出租车视频监控任务信息", notes = "参数为视频监控任务对象")
    public MessageResp getRecord(HttpServletRequest request, @ModelAttribute TaxiMonitorTask param) {
        MessageResp resp = new MessageResp();
        PageInfo pageInfo = null;
        try {
            pageInfo = taxiMonitorTaskService.selectByParam(param);
            if (pageInfo != null) {
                resp.setData(pageInfo.getList());
                resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            }
            resp.setResultDesc("查询成功");
            resp.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            logger.error("查询异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("查询异常");
        }
        return resp;
    }

    @PostMapping("/addTask")
    @ApiOperation(value = "添加出租车视频监控任务信息", notes = "参数为视频监控任务对象")
    public MessageResp addRecord(HttpServletRequest request, @RequestBody TaxiMonitorTask record) {
        MessageResp resp = new MessageResp();
        int addRes = 0;
        int size = 0;
        AuthUserResp authUser = sessionHandler.getUser(request);
        if (authUser != null) {
            record.setCreateBy(authUser.getUserId());
        }
        //判断开始时间和结束时间是否正确
        if (record != null && record.getStartTime() != null) {
            long time = record.getStartTime().getTime();
            long now = System.currentTimeMillis();
            if (time < now) {
                return CommonUtils.returnErrorInfo("新增失败,开始时间不能小于当前系统时间");
            }
            if (record.getEndTime() != null) {
                long endTime = record.getEndTime().getTime();
                if (endTime < time) {
                    return CommonUtils.returnErrorInfo("新增失败,结束时间不能小于开始时间");
                }
            }
        }

        TaxiMonitorTask task = new TaxiMonitorTask();
        //如果存在状态为01(未开始)的任务不允许新增
        task.setStatus(Constants.MONITOR_TASK_NOT);
        size = taxiMonitorTaskService.selectByParam(task).getSize();
        if (size > 0) {
            return CommonUtils.returnErrorInfo("新增失败,已存在未开始的任务");
        }
        //如果存在状态 02(进行中) 且自动创建任务为 1(是) 的数据,不允许新增
        task.setStatus(Constants.MONITOR_TASK_ING);
        task.setWhetherSelf(1);
        size = taxiMonitorTaskService.selectByParam(task).getSize();
        if (size > 0) {
            return CommonUtils.returnErrorInfo("新增失败,进行中任务已开启自动创建任务");
        }
        //查询当前状态为02(进行中)的任务结束时间
        task.setStatus(Constants.MONITOR_TASK_ING);
        task.setWhetherSelf(null);
        PageInfo<TaxiMonitorTask> pageInfo = taxiMonitorTaskService.selectByParam(task);
        TaxiMonitorTask tmt = new TaxiMonitorTask();
        if (pageInfo != null && pageInfo.getSize() > 0) {
            tmt = pageInfo.getList().get(0);
        }
        if (record != null) {
            if (tmt != null && tmt.getEndTime() != null && record.getStartTime() != null) {
                long endTime = tmt.getEndTime().getTime();
                long time = record.getStartTime().getTime();
                if (time < endTime) {
                    return CommonUtils.returnErrorInfo("新增失败，任务时间不能重合");
                }
            }
        }

        try {
            Long uuid = uuidUtils.getLongUUID();
            record.setUuid(uuid);
            record.setMonitorDuration(record.getMonitorDuration() * 60);
            addRes = taxiMonitorTaskService.addRecord(record);
            if (addRes > 0) {
                resp.setData(addRes);
                resp.setResultDesc("添加成功");
                resp.setResult(Boolean.TRUE.toString());
            }
        } catch (Exception e) {
            logger.error("添加异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("新增失败");
        }
        return resp;
    }

    @PostMapping("/updateTask")
    @ApiOperation(value = "更新出租车视频监控任务信息", notes = "参数为出租车视频监控任务信息对象")
    public MessageResp updateRecord(HttpServletRequest request, @RequestBody TaxiMonitorTask record) {
        MessageResp resp = new MessageResp();
        //查询出 旧的记录
        TaxiMonitorTask monitorTask = null;
        List<TaxiMonitorTask> oldTasks = null;
        if (record != null && record.getUuid() != null) {
            TaxiMonitorTask param = new TaxiMonitorTask();
            param.setUuid(record.getUuid());

            oldTasks = taxiMonitorTaskService.queryTask(param);
            if (oldTasks != null && oldTasks.size() > 0) {
                monitorTask = oldTasks.get(0);
            }
        }
        int updateRes = 0;

        if (monitorTask != null && record != null) {
            if (record.getWhetherSelf() != null) {
                //修改为 开启自动创建任务
                if (record.getWhetherSelf() == 1) {
                    //查询是否有 未开始的任务
                    TaxiMonitorTask task = new TaxiMonitorTask();
                    task.setStatus(Constants.MONITOR_TASK_NOT);
                    List<TaxiMonitorTask> tasks = taxiMonitorTaskService.queryTask(task);
                    if (tasks != null && tasks.size() > 0 && Constants.MONITOR_TASK_ING.equals(monitorTask.getStatus())) {
                        TaxiMonitorTask taxiMonitorTask = tasks.get(0);
                        if (taxiMonitorTask != null)
                            return CommonUtils.returnErrorInfo("请先删除新的任务，才能开启自动");
                    }
                }
            }

            if (record.getDuration() != null) {
                long f = (long) (record.getDuration() * 1000 * 60 * 60);
                long l = monitorTask.getStartTime().getTime();
                long v = f + l;
                long now = System.currentTimeMillis();
                if (v <= now) {
                    return CommonUtils.returnErrorInfo("修改失败,结束时间不能小于当前系统时间");
                }
                record.setEndTime(new Date(v));
                //修改轮询时间
                TaxiMonitorTask task = new TaxiMonitorTask();
                task.setStatus(Constants.MONITOR_TASK_NOT);
                List<TaxiMonitorTask> tasks = taxiMonitorTaskService.queryTask(task);
                if (tasks != null && tasks.size() > 0) {
                    TaxiMonitorTask taxiMonitorTask = tasks.get(0);//获取未开始的任务
                    if (taxiMonitorTask != null && !taxiMonitorTask.getUuid().equals(monitorTask.getUuid())) {
                        long time = taxiMonitorTask.getStartTime().getTime();
                        if (v > time) {
                            return CommonUtils.returnErrorInfo("修改失败,结束时间与未开始任务开始时间重合");
                        }
                    }
                }
            }

            try {
                updateRes = taxiMonitorTaskService.updateRecord(record);
                resp.setData(updateRes);
                resp.setResult(Boolean.TRUE.toString());
                resp.setResultDesc("更新成功");
            } catch (Exception e) {
                logger.error("更新异常,{}", e.getMessage());
                return CommonUtils.returnErrorInfo("更新失败");
            }
        } else {
            return CommonUtils.returnErrorInfo("更新失败");
        }
        return resp;
    }

    @DeleteMapping("/deleteTask/{uuid}")
    @ApiOperation(value = "删除出租车视频监控任务信息", notes = "参数UUID")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", dataType = "String", name = "uuid", value = "任务UUID", required = true)})
    public MessageResp deleteRecord(@PathVariable(value = "uuid") String uuid) {
        MessageResp resp = new MessageResp();
        int delRes = 0;
        TaxiMonitorTask task = new TaxiMonitorTask();
        task.setUuid(Long.parseLong(uuid));
        TaxiMonitorTask taxiMonitorTask = new TaxiMonitorTask();
        PageInfo<TaxiMonitorTask> pageInfo = taxiMonitorTaskService.selectByParam(task);
        if (pageInfo.getSize() > 0) {
            taxiMonitorTask = pageInfo.getList().get(0);
        }
        if (taxiMonitorTask != null && !Constants.MONITOR_TASK_NOT.equals(taxiMonitorTask.getStatus())) {
            return CommonUtils.returnErrorInfo("删除失败,任务状态不为未开始");
        }
        try {
            delRes = taxiMonitorTaskService.deleRecord(Long.parseLong(uuid));
            resp.setData(delRes);
            resp.setResultDesc("删除成功");
            resp.setResult(Boolean.TRUE.toString());
        } catch (Exception e) {
            logger.error("删除异常,{}", e.getMessage());
            return CommonUtils.returnErrorInfo("删除失败");
        }
        return resp;
    }

}
