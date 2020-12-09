package com.zhcx.authorization.controller.taxi;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.common.IotOperationCommon;
import com.zhcx.authorization.config.IotConfig;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.socketIO.MessgeBean;
import com.zhcx.authorization.socketIO.SocketIOResponse;
import com.zhcx.authorization.utils.*;
import com.zhcx.basicdata.facade.taxi.*;
import com.zhcx.basicdata.pojo.taxi.*;
import com.zhcx.common.util.UUIDUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/taxi/alarmOperate")
@Api(value = "taxiAlarmOperate", tags = "出租车警情处理接口")
public class TaxiAlarmOperateController extends SocketIOResponse {

    private Logger log = LoggerFactory.getLogger(TaxiAlarmOperateController.class);

    @Autowired
    private IotConfig iotConfig;

    @Autowired
    private IotOperationCommon iotOperationCommon;

    @Autowired
    private TaxiAlarmOperateService alarmOperateService;

    @Autowired
    private TaxiAlarmInfoService taxiAlarmInfoService;

    @Autowired
    private TaxiBaseInfoVehicleService vehicleService;

    @Autowired
    private TaxiTermianlVehicleService termianlVehicleService;

    @Autowired
    private TaxiBaseInfoDriverService driverService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @Autowired
    private TaxiAlarmLogService alarmLogService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UUIDUtils uuidUtils;

    @GetMapping("/queryAlarmList")
    @ApiOperation(value = "查看警情处理列表分页", notes = "参数status，取回状态")
    public MessageResp queryAlarmList(@ModelAttribute TaxiAlarmOperate param) {
        MessageResp resp = new MessageResp();
        try {
            PageInfo<TaxiAlarmOperate> pageInfo = alarmOperateService.queryAlarmList(param);
            resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            resp.setData(pageInfo.getList());
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询警情处理列表成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("查询警情处理列表异常");
        }
        return resp;
    }


    @GetMapping("/queryAlarmInfo")
    @ApiOperation(value = "警情处理详情", notes = "uuid")
    public MessageResp queryAlarmInfo(@RequestParam Long uuid) {
        MessageResp resp = new MessageResp();
        try {
            TaxiAlarmOperate alarmInfo = alarmOperateService.queryAlarmInfo(uuid);
            resp.setData(alarmInfo);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询警情处理详情成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("查询警情处理详情异常");
        }
        return resp;
    }

    @PutMapping("/updateAlarmInfo")
    @ApiOperation(value = "更新警情处理信息", notes = "参数alarm")
    public MessageResp updateAlarmInfo(HttpServletRequest request, @RequestBody TaxiAlarmOperate alarm) {
        MessageResp resp = new MessageResp();
        try {
            AuthUserResp user = sessionHandler.getUser(request);
            alarm.setUpdateBy(user.getUserName());
            alarm.setUpdateTime(new Date());
            if (StringUtils.isNotBlank(alarm.getHandlingResults())) {
                alarm.setResponsibleOfficerName(user.getUserName());//更新警情处理人
                alarm.setHandlingEndingTime(DateUtil.getYMDHMSFormat(new Date()));//更新警情处理时间
                alarm.setStatus(Constants.ALARM_STATUS_OVER);//处理状态
                //更新日志
                List<TaxiAlarmLog> alarmLogs = alarmLogService.selectByAlarmUuid(alarm.getUuid());
                if ( alarmLogs.size() > 0) {
                    try {
                        TaxiAlarmLog alarmLog = alarmLogs.get(0);
                        StringBuilder logStr = new StringBuilder(alarmLog.getOperationLog());
                        logStr.append(DateUtil.getYMDHMSFormat(new Date())).append(" ").append("监控坐席人员[运管处")
                                .append(user.getUserName()).append("]处理结果为[").append(alarm.getHandlingResults())
                                .append("];");
                        alarmLog.setOperationLog(logStr.toString());
                        alarmLogService.updateAlarmlog(alarmLog);
                    } catch (Exception e) {
                        log.error("更新警情处理时更新处理日志异常:" + e.getMessage());
                    }
                }
            }
            alarmOperateService.updateAlarmOperate(alarm);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("更新警情处理信息成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setStatusCode("00");
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("更新警情处理信息异常");
        }
        return resp;
    }

    @PostMapping("/addAlarmInfo")
    @ApiOperation(value = "添加警情处理信息", notes = "参数alarm")
    public MessageResp addAlarmInfo(HttpServletRequest request, @RequestBody TaxiAlarmOperate alarm) {
        MessageResp resp = new MessageResp();
        try {
            AuthUserResp user = sessionHandler.getUser(request);
            //1.判断车牌号是否存在
            String plateNumber = alarm.getPlateNumber();
            List<TaxiBaseInfoVehicle> vehicles = vehicleService.selectByPlateNumber(plateNumber);
            if (vehicles.size() < 1) {
                return CommonUtils.returnErrorInfo("添加失败,系统不存在此车辆");
            } else {
                //2.存在
                TaxiBaseInfoVehicle vehicle = vehicles.get(0);
                Long alarmUuid = uuidUtils.getLongUUID();//警情处理表id
                alarm.setUuid(alarmUuid);
                alarm.setVehicleId(vehicle.getUuid());
                alarm.setAlarmStartTime(new Date());
                Date current = new Date();
                alarm.setCreateBy(user.getUserName());
                alarm.setCreateTime(current);
                alarm.setUpdateBy(user.getUserName());
                alarm.setUpdateTime(current);
                alarm.setConfirmationType(Constants.ALARM_AFFIRM_NOT);//警情未确认
                alarm.setStatus(Constants.ALARM_STATUS_NOT);//未处理状态
                int res = alarmOperateService.addAlarmOperate(alarm);
                if (res > 0) {
                    resp.setStatusCode("00");
                    resp.setResult(Boolean.TRUE.toString());
                    resp.setResultDesc("添加警情处理信息成功");
                    //3.添加操作日志
                    try {
                        TaxiAlarmLog alarmLog = new TaxiAlarmLog();
                        alarmLog.setAlarmId(alarmUuid);
                        alarmLog.setCreateTime(DateUtil.getYMDHMSFormat(new Date()));
                        alarmLog.setUpdateTime(DateUtil.getYMDHMSFormat(new Date()));
                        alarmLog.setStartTime(DateUtil.getYMDHMSFormat(new Date()));
                        alarmLog.setDeviceId(vehicle.getDeviceId());
                        alarmLog.setCreateBy(user.getUserName());
                        StringBuilder logStr = new StringBuilder();
                        logStr.append(DateUtil.getYMDHMSFormat(new Date())).append(" ").append("监控坐席人员[")
                                .append(user.getUserName()).append("]通过");
                        if (Constants.ALARM_TYPE_PHONE.equals(alarm.getAlarmSource())) {
                            logStr.append("电话报警发现警情;");
                        } else if (Constants.ALARM_TYPE_RESTS.equals(alarm.getAlarmSource())) {
                            logStr.append("其他途径发现警情;");
                        }
                        alarmLog.setOperationLog(logStr.toString());
                        alarmLogService.insertAlarmLog(alarmLog);
                    } catch (Exception e) {
                        log.error("添加警情处理信息时添加操作日志异常:" + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("添加警情处理信息异常");
        }
        return resp;
    }

    //客服点击疑似险情或误报警时生成一条警情处理信息
    @PostMapping("/autoAddAlarmInfo")
    @ApiOperation(value = "客服点击弹框添加警情处理信息", notes = "参数TaxiAlarmInfo对象")
    public MessageResp autoAddAlarmInfo(HttpServletRequest request, @RequestBody TaxiAlarmInfo param) {
        MessageResp resp = new MessageResp();

        if (param != null) {
            String deviceId = param.getDeviceId();
            if (StringUtils.isBlank(deviceId)) {
                return CommonUtils.returnErrorInfo("操作失败");
            }
            try {
                AuthUserResp user = sessionHandler.getUser(request);

                Long alarmId = param.getAlarmId();//获取警情处理id
                TaxiAlarmOperate alarm = alarmOperateService.queryAlarmInfo(alarmId);
                if (alarm != null) {
                    alarm.setCreateBy(user.getUserName());
                    alarm.setUpdateBy(user.getUserName());
                    alarm.setResponsibleOfficerName(user.getUserName());
                    alarm.setHandlingStartingTime(DateUtil.getYMDHMSFormat(new Date()));//警情处理开始时间
                    alarm.setConfirmationType(param.getConfirmationType());//报警确认类型
                    if (StringUtils.equals(Constants.ALARM_AFFIRM_YES, param.getConfirmationType())) {
                        alarm.setStatus(Constants.ALARM_STATUS_NOT);//未处理
                    } else if (StringUtils.equals(Constants.ALARM_AFFIRM_NO, param.getConfirmationType())) {
                        alarm.setStatus(Constants.ALARM_STATUS_OVER);//已处理
                        alarm.setHandlingEndingTime(DateUtil.getYMDHMSFormat(new Date()));//处理结束时间
                    }
                    alarm.setPreConfirmationTime(DateUtil.getYMDHMSFormat(new Date()));//预警确认时间
                    alarm.setRemark(param.getRemark());//备注
                    int res = alarmOperateService.updateAlarmOperate(alarm);
                    if (res > 0) {
                        //更新日志
                        List<TaxiAlarmLog> alarmLogs = alarmLogService.selectByAlarmUuid(alarmId);
                        if (alarmLogs.size() > 0) {
                            TaxiAlarmLog alarmLog = alarmLogs.get(0);
                            String operationLog = alarmLog.getOperationLog();
                            StringBuilder operationLogStr = new StringBuilder(operationLog);
                            //判断警情确认类型
                            if (StringUtils.equals(Constants.ALARM_AFFIRM_YES, param.getConfirmationType())) {//疑似险情
                                operationLogStr.append(DateUtil.getYMDHMSFormat(new Date())).append(" ").append("监控坐席人员[客服")
                                        .append(user.getUserName()).append("]").append("确认为疑似险情,正在下一步处理;");

                            } else if (StringUtils.equals(Constants.ALARM_AFFIRM_NO, param.getConfirmationType())) {//误报警
                                operationLogStr.append(DateUtil.getYMDHMSFormat(new Date())).append(" ").append("监控坐席人员[客服")
                                        .append(user.getUserName()).append("]").append("确认为误报警,正在下一步处理;");
                            }
                            alarmLog.setOperationLog(operationLogStr.toString());//添加操作日志
                            alarmLog.setCreateBy(alarm.getCreateBy());//创建人
                            alarmLog.setAccessoryUrl(param.getAccessoryUrl());//图片地址
                            alarmLogService.updateAlarmlog(alarmLog);
                        }

                        //向iot发送解除预警指令
                        try {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    int flag = handleAlarm("35595", deviceId);
                                    if (flag != 0) {
                                        //指令下发成功
                                        log.debug("客服处理预警弹窗消息,下发向iot下发解除预警指令成功!");
                                    }
                                }
                            }).start();
                        } catch (Exception e) {
                            log.info("客服处理预警弹窗消息,下发向iot下发解除预警指令异常" + e.getMessage());
                        }


                        //修改报警表 报警标示位状态为 0
                        TaxiAlarmInfo taxiAlarmInfo = new TaxiAlarmInfo();
                        taxiAlarmInfo.setDeviceId(deviceId);
                        taxiAlarmInfo.setStartFlag(Constants.ALARM_VAL_YES);
                        taxiAlarmInfo.setStartFlagStatus(Constants.ALARM_VAL_YES);
                        List<TaxiAlarmInfo> infos = taxiAlarmInfoService.queryAlarms(taxiAlarmInfo);
                        if (infos.size() > 0) {
                            TaxiAlarmInfo alarmInfo = infos.get(0);
                            alarmInfo.setStartFlagStatus(Constants.ALARM_VAL_NO);
                            int p = taxiAlarmInfoService.updateAlarm(alarmInfo);
                            if (p > 0) {
                                log.debug("修改预警表中的状态为 0:正常 " + alarmInfo.toString());
                                //4.删除Redis中List对应的第一条数据
                                log.debug("删除redis中的预警消息:" + deviceId);
                                redisTemplate.opsForValue().set(deviceId, deviceId, 3, TimeUnit.MINUTES);
                            }
                        }

                    }
                }
                redisTemplate.boundListOps("alarmList1").leftPop();//弹出最左边的元素
                //向前端推送新的list
                List<TaxiAlarmInfo> alarmList = redisTemplate.boundListOps("alarmList1").range(0, -1);
                log.debug("推送至前端的弹窗预警数据List:" + alarmList.toString());
                Set<String> clients = getClient(ALARMEVENT, ALARMMONITOR);
                MessgeBean bean = new MessgeBean();
                bean.setContent(alarmList);
                bean.setType(ALARMMONITOR_PUSH);
                pushData(bean.getType(), bean, clients, "/alarm");
                resp.setStatusCode("00");
                resp.setResult(Boolean.TRUE.toString());
                resp.setResultDesc("操作成功");
            } catch (Exception e) {
                log.error("添加警情处理信息异常:" + e.getMessage());
                resp.setResult(Boolean.FALSE.toString());
                resp.setResultDesc("操作失败");
            }
        }
        return resp;
    }

    //向iot发送指令解除预警
    private int handleAlarm(String handleType, String deviceId) {
        String url = iotConfig.getTtsUrl() + "/" + deviceId;
        int res = 0;
        try {
            JSONObject ttsParam = packingParam(handleType);
            String token = iotOperationCommon.doLoginIot();
            if (null == token || "".equals(token)) {
                return res;
            }
            //发送请求
            Map<String, String> header = iotOperationCommon.getHeaderMap(token);
            int postRes = HttpClientUtils.getHttpResponseCode(url, "POST", header, ttsParam.toString());
            res = postRes;
        } catch (Exception e) {
            log.error("向iot下发指令异常:" + e.getMessage());
        }
        return res;
    }


    private JSONObject packingParam(String msgType) throws Exception {
        JSONObject result = new JSONObject();
        result.put("method", msgType);
        return result;
    }

    @GetMapping("/queryAlarmLog/{alarmId}")
    @ApiOperation(value = "查看警情处理日志", notes = "参数alarmId，警情处理id")
    public MessageResp queryAlarmLog(@PathVariable(value = "alarmId") Long alarmId) {
        MessageResp resp = new MessageResp();
        TaxiAlarmLog alarmLog = new TaxiAlarmLog();
        try {
            List<TaxiAlarmLog> alarmLogs = alarmLogService.selectByAlarmUuid(alarmId);
            if (alarmLogs.size() > 0) {
                alarmLog = alarmLogs.get(0);
                TaxiAlarmOperate alarmOperate = alarmOperateService.queryAlarmInfo(alarmId);
                if (alarmOperate != null) {
                    alarmLog.setPlateNumber(alarmOperate.getPlateNumber());//车牌
                    //组装驾驶员信息
                    if (alarmOperate.getDriverId() != null) {
                        TaxiBaseInfoDriver driver = driverService.selectDriver(alarmOperate.getDriverId());
                        if (driver != null) {
                            alarmLog.setPhoneNumber(driver.getPhoneNumber());//电话
                            alarmLog.setIdCertificateNumber(driver.getIdCertificateNumber());//身份证
                            alarmLog.setCertificateNumber(driver.getCertificateNumber());//运营证号
                            alarmLog.setDriverName(driver.getName());//名称
                            alarmLog.setPhoto(driver.getPhoto());//照片
                        }
                    }
                    //查询终端号
                    if (alarmOperate.getVehicleId() != null) {
                        TaxiVehicleTerminal vehicleTerminal = termianlVehicleService.selectByVehId(alarmOperate.getVehicleId());
                        if (vehicleTerminal != null) {
                            alarmLog.setTerminalCode(vehicleTerminal.getTerminalCode());//终端号
                            alarmLog.setCorpName(vehicleTerminal.getCorpName());//企业名
                        }
                    }
                }
            }
            resp.setData(alarmLog);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
//            resp.setResultDesc("查询警情处理日志成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("查询警情处理日志异常");
        }
        return resp;
    }


    @DeleteMapping("/deleteAlarmInfo")
    @ApiOperation(value = "删除警情处理信息", notes = "参数uuid")
    public MessageResp deleteAlarmInfo(@RequestParam Long uuid) {
        MessageResp resp = new MessageResp();
        try {
            alarmOperateService.deleteAlarmOperate(uuid);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("删除警情处理信息成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("删除警情处理信息异常");
        }
        return resp;
    }

    @PutMapping("/dealWithAlarm")
    @ApiOperation(value = "警情确认和处理", tags = "参数confirmationType，status")
    public MessageResp dealWithAlarm(HttpServletRequest request, Long uuid, String confirmationType, String handlingResults) {
        MessageResp resp = new MessageResp();
        try {
            AuthUserResp user = sessionHandler.getUser(request);
            TaxiAlarmOperate alarm = new TaxiAlarmOperate();
            alarm.setUuid(uuid);
            if (StringUtils.isNotBlank(handlingResults)) {
                alarm.setHandlingResults(handlingResults);
                alarm.setStatus("30");
            }
            if (StringUtils.isNotBlank(confirmationType)) {
                alarm.setConfirmationType(confirmationType);
                //误报警
                if (StringUtils.equals(confirmationType, "20")) {
                    alarm.setStatus("30");
                } else if (StringUtils.isBlank(handlingResults)) {//真实报警
                    alarm.setStatus("20");
                }
            }
            alarm.setUpdateBy(user.getUserName());
            alarm.setUpdateTime(new Date());
            alarmOperateService.dealWithAlarm(alarm);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("处理成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setResultDesc("处理异常");
        }
        return resp;
    }
}
