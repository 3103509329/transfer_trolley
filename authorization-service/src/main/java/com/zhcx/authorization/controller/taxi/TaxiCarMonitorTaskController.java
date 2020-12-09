package com.zhcx.authorization.controller.taxi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.*;
import com.zhcx.basicdata.facade.taxi.TaxiBaseInfoDriverService;
import com.zhcx.basicdata.facade.taxi.TaxiCarMonitorTaskService;
import com.zhcx.basicdata.facade.taxi.TaxiMonitorTaskService;
import com.zhcx.basicdata.facade.taxi.TaxiViolateRecordService;
import com.zhcx.basicdata.pojo.taxi.TaxiCarMonitorTask;
import com.zhcx.basicdata.pojo.taxi.TaxiMonitorTask;
import com.zhcx.basicdata.pojo.taxi.TaxiViolateRecord;
import com.zhcx.common.util.UUIDUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/taxi/carMonitor")
@Api(value = "carMonitorTask", tags = "出租车车辆监控任务接口")
public class TaxiCarMonitorTaskController {

    private Logger log = LoggerFactory.getLogger(TaxiCarMonitorTaskController.class);

    @Autowired
    private TaxiCarMonitorTaskService taxiCarMonitorTaskService;

    @Autowired
    private TaxiMonitorTaskService taxiMonitorTaskService;

    @Autowired
    private TaxiViolateRecordService taxiViolateRecordService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @Autowired
    private TaxiBaseInfoDriverService driverService;

    @Autowired
    private UUIDUtils uuidUtils;

    @Autowired
    private HttpUtils httpUtils;

    @GetMapping("/queryCarMonitorPage")
    @ApiOperation(value = "查询车辆监控列表(包含任务/记录)", notes = "参数TaxiCarMonitorTask")
    public MessageResp queryCarMonitorPage(HttpServletRequest request, @ModelAttribute TaxiCarMonitorTask param) {
        MessageResp resp = new MessageResp();
        PageInfo<TaxiCarMonitorTask> pageInfo = null;
        try {
           /* AuthUserResp authUser = sessionHandler.getUser(request);
            
            if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
            	 resp.setResult(Boolean.FALSE.toString());
                 resp.setStatusCode("-50");
                 resp.setResultDesc("没有权限查看");
                 return resp;
            }*/
            if (!"".equals(param.getStatusStrs()) && null != param.getStatusStrs()) {
                param.setStatusArray(param.getStatusStrs().split(","));
            }
            pageInfo = taxiCarMonitorTaskService.queryCarMonitorTaskPage(param);
            resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            resp.setData(pageInfo.getList());
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询车辆监控任务列表成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询车辆监控任务列表异常");
        }
        return resp;
    }


    @GetMapping("/queryCarMonitorTaskPage")
    @ApiOperation(value = "查询车辆监控任务列表（任务）", notes = "参数TaxiCarMonitorTask")
    public MessageResp queryCarMonitorTaskPage(HttpServletRequest request, @ModelAttribute TaxiCarMonitorTask param) {
        MessageResp resp = new MessageResp();
        PageInfo<TaxiCarMonitorTask> pageInfo = null;
        try {
           /* AuthUserResp authUser = sessionHandler.getUser(request);
            
            if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
            	 resp.setResult(Boolean.FALSE.toString());
                 resp.setStatusCode("-50");
                 resp.setResultDesc("没有权限查看");
                 return resp;
            }*/
            List<TaxiMonitorTask> taskList = null;
            TaxiMonitorTask taskRecord = new TaxiMonitorTask();
            taskRecord.setStatus(Constants.MONITOR_TASK_ING);
            taskList = taxiMonitorTaskService.queryTask(taskRecord);
            if (null != taskList && taskList.size() == 1) {
                param.setTaskId(taskList.get(0).getUuid());
            } else {
                resp.setResult(Boolean.TRUE.toString());
                resp.setStatusCode("00");
                resp.setResultDesc("当前没有进行中的任务");
                return resp;
            }

            if (!"".equals(param.getStatusStrs()) && null != param.getStatusStrs()) {
                param.setStatusArray(param.getStatusStrs().split(","));
            }
            pageInfo = taxiCarMonitorTaskService.queryCarMonitorTaskPage(param);
            resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            resp.setData(pageInfo.getList());
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询车辆监控任务列表成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询车辆监控任务列表异常");
        }
        return resp;
    }

    @GetMapping("/queryCarMonitorTaskList")
    @ApiOperation(value = "查询车辆监控任务，不带分页", notes = "参数TaxiCarMonitorTask")
    public MessageResp queryCarMonitorTaskList(@ModelAttribute TaxiCarMonitorTask param) {
        MessageResp resp = new MessageResp();
        try {
            List<TaxiMonitorTask> taskList = null;
            TaxiMonitorTask taskRecord = new TaxiMonitorTask();
            taskRecord.setStatus(Constants.MONITOR_TASK_ING);
            taskList = taxiMonitorTaskService.queryTask(taskRecord);
            if (null != taskList && taskList.size() == 1) {
                param.setTaskId(taskList.get(0).getUuid());
            }
            param.setStatusArray(new String[]{Constants.CAR_MONITOR_TASK_ING, Constants.CAR_MONITOR_TASK_OVER});
            List<TaxiCarMonitorTask> list = taxiCarMonitorTaskService.queryCarMonitorTaskList(param);
            resp.setResult(Boolean.TRUE.toString());
            resp.setStatusCode("00");
            resp.setResultDesc("查询车辆监控任务成功");
            resp.setData(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询车辆监控任务异常");
        }
        return resp;
    }

    @GetMapping("/statUserCarMonitorTask")
    @ApiOperation(value = "统计用户当前车辆监控任务", notes = "参数:无")
    public Map statUserCarMonitorTask(HttpServletRequest request) {
        Map map = new HashMap();
        int violateSize = 0;
        AuthUserResp authUser = sessionHandler.getUser(request);
        try {
            List<TaxiMonitorTask> taskList = null;
            TaxiMonitorTask taskRecord = new TaxiMonitorTask();
            taskRecord.setStatus(Constants.MONITOR_TASK_ING);
            taskList = taxiMonitorTaskService.queryTask(taskRecord);
            //判断任务数是否有唯一一条进行中
            if (null != taskList && taskList.size() == 1) {
                TaxiCarMonitorTask param = new TaxiCarMonitorTask();
                //param.setStatusArray(new String[]{Constants.CAR_MONITOR_TASK_OVER});
                param.setCreateBy(authUser.getUserId());
                param.setTaskId(taskList.get(0).getUuid());
                List<TaxiCarMonitorTask> list = taxiCarMonitorTaskService.queryCarMonitorTaskCount(param);
                //需要查询所有状态下抓拍的违规记录
                //param.setStatusArray(null);
                //List<TaxiCarMonitorTask> allList = taxiCarMonitorTaskService.queryCarMonitorTaskList(param);
                List<TaxiCarMonitorTask> allList = list.stream().filter(TaxiCarMonitorTask->Constants.CAR_MONITOR_TASK_OVER.equals(TaxiCarMonitorTask.getStatus())).collect(Collectors.toList());
                for (TaxiCarMonitorTask taxiCarMonitorTask : allList) {
                    TaxiViolateRecord record = new TaxiViolateRecord();
                    record.setTaskId(taxiCarMonitorTask.getUuid());
                    record.setMonitorId(taxiCarMonitorTask.getTaskId());
                    List violates = taxiViolateRecordService.queryRecord(record);
                    violateSize = violateSize + violates.size();
                }
                map.put("createBname", authUser.getUserName());
                map.put("taskId", taskList.get(0).getUuid());
                map.put("carTaskSize", list.size());
                map.put("violateSize", violateSize);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    @GetMapping("/checkUserCarMonitorTask")
    @ApiOperation(value = "判断用户进行中的任务数", notes = "参数:无")
    public MessageResp checkUserCarMonitorTask(HttpServletRequest request) {
        MessageResp resp = new MessageResp();
        try {
            AuthUserResp authUser = sessionHandler.getUser(request);

            List<TaxiMonitorTask> taskList = null;
            TaxiMonitorTask taskRecord = new TaxiMonitorTask();
            taskRecord.setStatus(Constants.MONITOR_TASK_ING);
            taskList = taxiMonitorTaskService.queryTask(taskRecord);

            //判断任务数是否有唯一一条进行中
            if (null != taskList && taskList.size() == 1) {
                TaxiCarMonitorTask param = new TaxiCarMonitorTask();
                //param.setStatus(Constants.CAR_MONITOR_TASK_ING);
                param.setStatusArray(new String[]{Constants.CAR_MONITOR_TASK_ING});
                param.setCreateBy(authUser.getUserId());
                param.setTaskId(taskList.get(0).getUuid());
                List<TaxiCarMonitorTask> list = taxiCarMonitorTaskService.queryCarMonitorTaskCount(param);
                if (list.size() >= 6) {
                    resp.setResult(Boolean.TRUE.toString());
                    resp.setStatusCode("-50");
                    resp.setResultDesc("监控任务已超出最大数，请先完成未完成的任务");
                } else {
                    resp.setResult(Boolean.TRUE.toString());
                    resp.setStatusCode("00");
                    resp.setResultDesc("查询车辆监控任务成功");
                }
                return resp;
            } else {
                resp.setResult(Boolean.TRUE.toString());
                resp.setStatusCode("00");
                resp.setResultDesc("查询车辆监控任务成功");
                return resp;
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询车辆监控任务异常");
            return resp;
        }
    }


    @PostMapping("/insertCarMonTk")
    @ApiOperation(value = "添加（任务/查看）车辆任务", notes = "参数TaxiCarMonitorTask")
    public MessageResp insertCarMonTk(HttpServletRequest request, @RequestBody TaxiCarMonitorTask param) {
        MessageResp resp = new MessageResp();
        //是否是监控任务
        boolean temp = false;
        TaxiMonitorTask resultTask = null;
        Long driverId = null;
        String driverName = null;
        AuthUserResp authUser = sessionHandler.getUser(request);

        /**
         * 获取车辆当班驾驶员信息
         */
        try {
            String res = getWorkInDriverUuid(param.getVehicleId() + "");
            if (StringUtils.isNotBlank(res)) {
                String[] split = res.split(",");
                String driverUuid = split[0];

                //查询关联驾驶员,关联设备
                JSONObject data = driverService.qeuryDriverForMointor(param.getDeviceId(), driverUuid);
                if (null != data) {
                    driverId = data.get("uuid") != null ? Long.valueOf(data.get("uuid").toString()) : null;
                    driverName = data.get("name") != null ? data.get("name").toString() : null;
                }

            }
        } catch (Exception e) {
            log.error("insertCarMonTk  查询签到驾驶员信息异常: " + e.getMessage());
        }

        try {

            //判断是进行中的任务还是新建任务/记录
            List<TaxiMonitorTask> taskList = null;
            TaxiMonitorTask taskRecord = new TaxiMonitorTask();
            taskRecord.setStatus(Constants.MONITOR_TASK_ING);
            taskList = taxiMonitorTaskService.queryTask(taskRecord);
            //判断任务数是否有唯一一条进行中
            if (null != taskList && taskList.size() == 1) {
                resultTask = taskList.get(0);

                TaxiCarMonitorTask carMonitorModel = new TaxiCarMonitorTask();
                //carMonrecordIng.setStatus(Constants.CAR_MONITOR_TASK_ING);
                //carMonrecord.setCreateBy(authUser.getUserId());
                //carMonrecordOver.setStatusArray(new String[]{Constants.CAR_MONITOR_TASK_OVER});
                carMonitorModel.setTaskId(resultTask.getUuid());
                carMonitorModel.setVehicleId(param.getVehicleId());
                List<TaxiCarMonitorTask> carMonitorList = taxiCarMonitorTaskService.queryCarMonitorTaskCount(carMonitorModel);
                
                
                List<TaxiCarMonitorTask> lsOver = carMonitorList.stream().filter(TaxiCarMonitorTask->Constants.CAR_MONITOR_TASK_OVER.equals(TaxiCarMonitorTask.getStatus())).collect(Collectors.toList());
                if (null != lsOver && lsOver.size() > 0) {//存在已完成任务
                    String time = DateUtil.getYMDHMSFormat(new Date());
                    param.setUuid(uuidUtils.getLongUUID());
                    param.setMonitorDuration(0);
                    param.setCreateBy(authUser.getUserId());
                    param.setCreateBname(authUser.getUserName());
                    param.setTaskStartTime(time);
                    param.setUpdateBy(authUser.getUserId());
                    param.setUpdateBname(authUser.getUserName());
                    param.setStartTime(time);
                    param.setDriverId(driverId);
                    param.setDriverName(driverName);
                    param.setType(Constants.CAR_MONITOR_RECORD);
                    taxiCarMonitorTaskService.save(param);
                    param.setCreateBname(lsOver.get(0).getCreateBname());
                    resp.setData(param);
                    resp.setResult(Boolean.TRUE.toString());
                    resp.setStatusCode("00");
                    resp.setResultDesc("添加车辆监控任务成功");
                    return resp;
                }

                //TaxiCarMonitorTask carMonrecordIng = new TaxiCarMonitorTask();
                //carMonrecordIng.setStatus(Constants.CAR_MONITOR_TASK_ING);
                //carMonrecord.setCreateBy(authUser.getUserId());
                //carMonrecordIng.setStatusArray(new String[]{Constants.CAR_MONITOR_TASK_ING});
                //carMonrecordIng.setTaskId(resultTask.getUuid());
               // carMonrecordIng.setVehicleId(param.getVehicleId());
                List<TaxiCarMonitorTask> lsIng = carMonitorList.stream().filter(TaxiCarMonitorTask->Constants.CAR_MONITOR_TASK_ING.equals(TaxiCarMonitorTask.getStatus())).collect(Collectors.toList());//taxiCarMonitorTaskService.queryCarMonitorTaskList(carMonrecordIng);
                if (null != lsIng && lsIng.size() > 0) {//存在进行中任务
                    TaxiCarMonitorTask task = lsIng.get(0);
                    task.setTaskDuration(resultTask.getMonitorDuration());
                    /*task.setDriverId(driverId);
                    task.setDriverName(driverName);*/
                    //resp.setData(task);
                    //当前打开用户不为任务监控人则清除返回对象中的任务监控时长
                    if (authUser.getUserId().equals(task.getCreateBy())) {
                        //task.setMonitorDuration(null);
                        resp.setData(task);
                        resp.setResult(Boolean.TRUE.toString());
                        resp.setStatusCode("00");
                        resp.setResultDesc("打开监控任务成功");
                        return resp;
                    } else {
                        String time = DateUtil.getYMDHMSFormat(new Date());
                        param.setUuid(uuidUtils.getLongUUID());
                        param.setMonitorDuration(0);
                        param.setCreateBy(authUser.getUserId());
                        param.setCreateBname(authUser.getUserName());
                        param.setTaskStartTime(time);
                        param.setUpdateBy(authUser.getUserId());
                        param.setUpdateBname(authUser.getUserName());
                        param.setStartTime(time);
                        param.setDriverId(driverId);
                        param.setDriverName(driverName);
                        param.setType(Constants.CAR_MONITOR_RECORD);
                        taxiCarMonitorTaskService.save(param);
                        param.setCreateBname(lsIng.get(0).getCreateBname());
                        resp.setData(param);
                        resp.setResult(Boolean.TRUE.toString());
                        resp.setStatusCode("00");
                        resp.setResultDesc("添加车辆监控任务成功");
                        return resp;
                    }

                }
                //判断是否存在已终止的任务，再判断已经终止的任务是不是跟现在打开任务的人一致。如果是，则修改终止状态为进行中
                //TaxiCarMonitorTask carMonrecordStop = new TaxiCarMonitorTask();
                //carMonrecordStop.setStatus(Constants.CAR_MONITOR_TASK_STOP);
                //carMonrecordStop.setStatusArray(new String[]{Constants.CAR_MONITOR_TASK_STOP});
                //carMonrecord.setCreateBy(authUser.getUserId());
                //carMonrecordStop.setTaskId(resultTask.getUuid());
                //carMonrecordStop.setVehicleId(param.getVehicleId());
                List<TaxiCarMonitorTask> lsStop = carMonitorList.stream().filter(TaxiCarMonitorTask->Constants.CAR_MONITOR_TASK_STOP.equals(TaxiCarMonitorTask.getStatus())).collect(Collectors.toList()); //taxiCarMonitorTaskService.queryCarMonitorTaskList(carMonrecordStop);
                if (null != lsStop && lsStop.size() > 0) {//存在终止状态的任务
                    for (TaxiCarMonitorTask taxiCarMonitorTask : lsStop) {
                        if (authUser.getUserId().equals(taxiCarMonitorTask.getCreateBy()) && Constants.AUTH_USER_TYPE_SYS_Ext.indexOf(authUser.getUserExt1()) != -1) {//终止任务的创建人是当前操作用户
                            taxiCarMonitorTask.setTaskDuration(resultTask.getMonitorDuration());
							/*taxiCarMonitorTask.setDriverId(driverId);
							taxiCarMonitorTask.setDriverName(driverName);*/
                            taxiCarMonitorTask.setStatus(Constants.CAR_MONITOR_TASK_ING);
                            resp.setData(taxiCarMonitorTask);
                            TaxiCarMonitorTask upModel = new TaxiCarMonitorTask();
                            upModel.setUuid(taxiCarMonitorTask.getUuid());
                            upModel.setStatus(Constants.CAR_MONITOR_TASK_ING);
                            taxiCarMonitorTaskService.modifySelective(upModel);
                            resp.setResult(Boolean.TRUE.toString());
                            resp.setStatusCode("00");
                            resp.setResultDesc("打开监控任务成功");
                            return resp;
                        }
                    }
                }

            

                //判断用户角色，如果是监控人员则设立为任务类型
	            if (Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())) {
	                if (StringUtils.isNotBlank(authUser.getUserExt1())) {
	                    if (authUser.getUserExt1().indexOf(Constants.AUTH_USER_TYPE_SYS_Ext) != -1) {
	                        
	                            TaxiCarMonitorTask carMonParam1 = new TaxiCarMonitorTask();
	                            carMonParam1.setStatusArray(new String[]{Constants.CAR_MONITOR_TASK_ING});
	                            carMonParam1.setCreateBy(authUser.getUserId());
	                            carMonParam1.setTaskId(resultTask.getUuid());
	                            List<TaxiCarMonitorTask> list = taxiCarMonitorTaskService.queryCarMonitorTaskCount(carMonParam1);
	                            if (list.size() < 6) {//当前用户已有未完成任务数必须是小于6条
	                            	//TaxiCarMonitorTask carMonParam = new TaxiCarMonitorTask();
                                    //carMonParam.setTaskId(resultTask.getUuid());
                                    //carMonParam.setVehicleId(param.getVehicleId());
                                    //carMonParam.setStatusArray(new String[]{Constants.CAR_MONITOR_TASK_ING});
                                    List<TaxiCarMonitorTask> carMonList = carMonitorList.stream().filter(TaxiCarMonitorTask->Constants.CAR_MONITOR_TASK_ING.equals(TaxiCarMonitorTask.getStatus())).collect(Collectors.toList()); //taxiCarMonitorTaskService.queryCarMonitorTaskList(carMonParam);
                                    if (null == carMonList || carMonList.size() < 1) {
	                                    temp = true;
	                                    param.setTaskId(resultTask.getUuid());
	                                    param.setType(Constants.CAR_MONITOR_TASK);
	                                    param.setStatus(Constants.CAR_MONITOR_TASK_ING);
				               			/* String time = DateUtil.getNowDateTime();
				               			 param.setCreateBy(authUser.getUserId());
				               			 param.setUpdateBy(authUser.getUserId());
				               			 param.setStartTime(time);
				               			 taxiCarMonitorTaskService.save(param);*/
	                                }
	                            }
	                            param.setTaskDuration(resultTask.getMonitorDuration());
	                    }
	                }
	            }
            }
            String time = DateUtil.getYMDHMSFormat(new Date());
            param.setUuid(uuidUtils.getLongUUID());
            param.setMonitorDuration(0);
            param.setCreateBy(authUser.getUserId());
            param.setCreateBname(authUser.getUserName());
            param.setTaskStartTime(time);
            param.setUpdateBy(authUser.getUserId());
            param.setUpdateBname(authUser.getUserName());
            param.setStartTime(time);
            param.setDriverId(driverId);
            param.setDriverName(driverName);
            if (!temp) {
                param.setType(Constants.CAR_MONITOR_RECORD);
            }

            taxiCarMonitorTaskService.save(param);
            if (null == resp.getData()) {
                resp.setData(param);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("添加车辆监控任务失败");
        }
        resp.setResult(Boolean.TRUE.toString());
        resp.setStatusCode("00");
        resp.setResultDesc("添加车辆监控任务成功");
        return resp;
    }

    @PutMapping("/updateCarMonTk")
    @ApiOperation(value = "更新（任务/查看）", notes = "参数TaxiCarMonitorTask")
    public MessageResp updateCarMonTk(HttpServletRequest request, @RequestBody TaxiCarMonitorTask param) {
        MessageResp resp = new MessageResp();
        try {
            AuthUserResp authUser = sessionHandler.getUser(request);
            List<TaxiMonitorTask> taskList = null;
            TaxiMonitorTask taskRecord = new TaxiMonitorTask();
            taskRecord.setStatus(Constants.MONITOR_TASK_ING);
            taskList = taxiMonitorTaskService.queryTask(taskRecord);

            TaxiCarMonitorTask carMonParam = new TaxiCarMonitorTask();
            carMonParam.setUuid(param.getUuid());
            List<TaxiCarMonitorTask> carMonList = taxiCarMonitorTaskService.queryCarMonitorTaskCount(carMonParam);

            if (null != taskList && taskList.size() > 0) {
                TaxiMonitorTask task = taskList.get(0);
                TaxiCarMonitorTask carMonTK = carMonList.get(0);
                if (null != carMonTK) {
                    if (Constants.CAR_MONITOR_TASK_OVER.equals(carMonTK.getStatus())) {
                        resp.setResult(Boolean.TRUE.toString());
                        resp.setStatusCode("00");
                        resp.setResultDesc("更新信息成功");
                        return resp;
                    }
                    if (Constants.CAR_MONITOR_TASK == carMonTK.getType()) {
                        if (Constants.CAR_MONITOR_TASK_OVER.equals(param.getStatus())) {
                            param.setMonitorDuration(task.getMonitorDuration());
                        }else {

                            Integer ckmd = carMonTK.getMonitorDuration();
                            Integer total = ckmd + param.getMonitorDuration();
                            param.setMonitorDuration(total);
                        }
                    }
                }

            }
            String time = DateUtil.getYMDHMSFormat(new Date());
            param.setTaskEndTime(time);
            param.setUpdateBy(authUser.getUserId());
            param.setUpdateBname(authUser.getUserName());
            if (taxiCarMonitorTaskService.modifySelective(param) > 0) {
                resp.setResult(Boolean.TRUE.toString());
                resp.setStatusCode("00");
                resp.setResultDesc("更新信息成功");
            } else {
                resp.setResult(Boolean.FALSE.toString());
                resp.setStatusCode("-50");
                resp.setResultDesc("更新信息失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("更新信息失败");
        }

        return resp;
    }

    protected String getWorkInDriverUuid(String vehicleId) throws Exception {

//      plateNum = plateNum.substring(plateNum.indexOf("湘")+1,plateNum.length());
        String result = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -5);// 5分钟之前的时间
        Date beforeD = beforeTime.getTime();
        String time = sdf.format(beforeD);

        StringBuilder sql = new StringBuilder();
        sql.append(" select tmp.DRIVER_ID,tmp.CHECKIN_TIME from ( ");
        sql.append("select CHECKIN_TIME,DRIVER_ID,max(__time) as checkin_time1 from taxi_check_in where ");
        sql.append(" VEHICLE_ID = '").append(vehicleId).append("'");
        sql.append(" group by DRIVER_ID,CHECKIN_TIME ) tmp  order by tmp.checkin_time1 DESC limit 1");
        JSONArray res = null;
        try {
            res = httpUtils.doPostSqlUrl("sql", sql.toString());
            if (res != null && res.size() > 0) {
                if (res.size() > 1) {
                    log.error("数据异常,当前车辆" + vehicleId + "同一时间存在两个驾驶员打卡记录");
                } else {
                    JSONObject obj = res.getJSONObject(0);
                    String checkinTime = obj.get("CHECKIN_TIME").toString();
                    result = obj.get("DRIVER_ID").toString() + "," + checkinTime;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
