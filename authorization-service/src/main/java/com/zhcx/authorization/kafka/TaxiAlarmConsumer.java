package com.zhcx.authorization.kafka;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhcx.authorization.socketIO.MessgeBean;
import com.zhcx.authorization.socketIO.SocketIOResponse;
import com.zhcx.authorization.utils.Constants;
import com.zhcx.authorization.utils.DateUtil;
import com.zhcx.authorization.utils.HttpUtils;
import com.zhcx.basicdata.facade.taxi.*;
import com.zhcx.basicdata.pojo.taxi.*;
import com.zhcx.common.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @description: 报警信息消费类
 * @author: qiuziqiang
 * @date 2019-06-24 17:28
 **/
@Component
public class TaxiAlarmConsumer extends SocketIOResponse {

    @Autowired
    private TaxiAlarmInfoService alarmInfoService;

    @Autowired
    private TaxiBaseInfoVehicleService vehicleService;

    @Autowired
    private TaxiAlarmBlackListService blackListService;

    @Autowired
    private TaxiAlarmOperateService alarmOperateService;

    @Autowired
    private TaxiAlarmLogService alarmLogService;

    @Autowired
    private TaxiBaseInfoDriverService driverService;

    @Autowired
    private UUIDUtils uuidUtils;

    @Autowired
    private HttpUtils httpUtils;

    @Resource
    private RedisTemplate redisTemplate;

    private static final Logger log = LoggerFactory.getLogger(TaxiAlarmConsumer.class);

    @KafkaListener(topics = "${alarm.consumer.topic}")
    public void alarmConsume(String msgContent) {
        log.debug("alarmConsume开始消费来自kafka的GPS消息(预警/报警信息)" + msgContent);
        JSONObject kafkaObj = JSONObject.parseObject(msgContent);
        if (kafkaObj.isEmpty()) {
            return;
        }
        String deviceId = kafkaObj.getString("DEVICE_ID");//获取的车辆deviceId
        String alarmVal = kafkaObj.getString("ALARM_VAL");//报警状态标示 0:正常 1:报警
        String alarmFlag = kafkaObj.getString("ALARM_FLAG");//报警类型 1:紧急报警 2:预警 3:卫星定位模块发生故障 4:...
        String startTime = kafkaObj.getString("TIME");//报警开始时间 毫秒值
        String longitude = kafkaObj.getString("LONGITUDE");//报警开始经度
        String latitude = kafkaObj.getString("LATITUDE");//报警开始纬度
        String speed = kafkaObj.getString("SPEED");//报警开始速度
        String direction = kafkaObj.getString("DIRECTION");//方向


        //判断是否为险情预警消息或者紧急报警消息
        if (StringUtils.equals(Constants.ALARM_VAL_YES, alarmVal)) {

            if (StringUtils.equals("0", alarmFlag) || StringUtils.equals("1", alarmFlag)) {
                //判断是否存在redis中
                Object obj = redisTemplate.opsForValue().get(deviceId);
                if (obj != null) {
                    return;
                }
                //判断此车辆是否存在黑名单
                TaxiAlarmBlackList blackList = new TaxiAlarmBlackList();
                blackList.setDeviceId(deviceId);
                List<TaxiAlarmBlackList> blackLists = blackListService.queryAlarmBlackList(blackList);
                if (blackLists.size() > 0) {
                    log.info("预警车辆存在于预警黑名单中  deviceId:" + deviceId);
                    return;
                }
                //判断在报警信息表中是否存在该报警车辆且报警状态标示和报警类型相同
                TaxiAlarmInfo alarmInfo = new TaxiAlarmInfo();
                alarmInfo.setStartFlag(Constants.ALARM_VAL_YES);//报警开始标示位
                alarmInfo.setStartFlagStatus(alarmVal);//报警开始标示位状态
                alarmInfo.setDeviceId(deviceId);//车辆deviceId
                List<TaxiAlarmInfo> alarmInfos = alarmInfoService.queryAlarms(alarmInfo);
                //存在 更新时间
                if (alarmInfos != null && alarmInfos.size() > 0) {
                    TaxiAlarmInfo taxiAlarmInfo = alarmInfos.get(0);
                    alarmInfoService.updateAlarm(taxiAlarmInfo);//更新时间
                    log.debug("更新时间,预警表中已存在该条记录:" + taxiAlarmInfo.toString());
                } else {
                    //不存在 存入报警信息表
                    Long alarmUuid = uuidUtils.getLongUUID();//警情处理表id
                    alarmInfo.setAlarmId(alarmUuid);
                    //根据deviceId查询系统中此车辆
                    List<TaxiBaseInfoVehicle> vehicles = vehicleService.selectByDeviceId(deviceId);
                    if (vehicles != null && vehicles.size() > 0) {
                        TaxiBaseInfoVehicle vehicle = vehicles.get(0);
                        String vehicleId = vehicle.getUuid();
                        alarmInfo.setVehicleUUID(vehicleId);
                        alarmInfo.setVideoDeviceId(vehicle.getVideoDeviceId());//设置车辆锐明视频id
                        //查询车辆打卡驾驶员信息
                        try {
                            String res = getWorkInVehicleId(vehicleId);
                            if (StringUtils.isBlank(res)) {
                                log.error("当前车辆的没有驾驶员打卡信息");
                            } else {
                                String[] split = res.split(",");
                                String driverId = split[0];
                                TaxiBaseInfoDriver driver = driverService.selectDriver(driverId);
                                if (driver != null) {
                                    alarmInfo.setDriverId(driverId);//当班驾驶员id
                                    alarmInfo.setDriverName(driver.getName());//驾驶员名称
                                    alarmInfo.setPhoneNumber(driver.getPhoneNumber());//驾驶员联系方式
                                    alarmInfo.setDriverWorkType(driver.getWorkType());//驾驶员班次状态
                                }
                            }
                        } catch (Exception e) {
                            log.error("查询报警车辆驾驶员打卡信息异常:", e.getMessage());
                        }
                        alarmInfo.setCorpId(vehicle.getCorpId());//公司id
                        alarmInfo.setVehiclePlateNumber(vehicle.getPlateNumber());//车牌号码
                        alarmInfo.setCorpName(vehicle.getCorpName());//公司名
                        alarmInfo.setUuid(uuidUtils.getLongUUID().intValue());//警情uuid
                        alarmInfo.setStartTime(new Date(Long.parseLong(startTime)));//报警开始时间
                        alarmInfo.setStartLongitude(Double.parseDouble(longitude));//经度
                        alarmInfo.setStartLatitude(Double.parseDouble(latitude));//纬度
                        alarmInfo.setStartSpeed(Double.parseDouble(speed));//速度
                        alarmInfo.setDirection(direction);
                        int res = alarmInfoService.insertAlarmInfo(alarmInfo);
                        //把报警类型为预警的消息加入到缓存
                        if (res > 0) {
                            redisTemplate.boundListOps("alarmList1").rightPush(alarmInfo);
                            log.debug("将预警消息存入redis/alarmList中:" + alarmInfo.toString());
                            //生成警情处理
                            TaxiAlarmOperate alarm = new TaxiAlarmOperate();
                            alarm.setUuid(alarmUuid);
                            alarm.setCreateTime(new Date());
                            alarm.setUpdateTime(new Date());
                            alarm.setAlarmSource(Constants.ALARM_TYPE_ONE_KEY); //一键报警
                            alarm.setPlateNumber(alarmInfo.getVehiclePlateNumber());//车牌号码
                            alarm.setAlarmPerson(alarmInfo.getDriverName());//报警人名称
                            alarm.setAlarmPersonPhone(alarmInfo.getPhoneNumber());//报警人电话
                            alarm.setAlarmStartTime(alarmInfo.getStartTime());//报警开始时间
                            alarm.setDriverId(alarmInfo.getDriverId());//驾驶员id
                            alarm.setConfirmationType("30");//报警确认类型 未确认
                            alarm.setDeviceId(deviceId);
                            alarm.setVehicleId(vehicleId);
                            //警情处理日志操作
                            StringBuilder operationLogStr = new StringBuilder();
                            operationLogStr.append(DateUtil.getYMDHMSFormat(alarmInfo.getStartTime()));
                            String driverName = null;
                            if (alarmInfo.getDriverName() == null || "".equals(alarmInfo.getDriverName())) {
                                driverName = "--";
                            } else {
                                driverName = alarmInfo.getDriverName();
                            }
                            operationLogStr.append(" ").append(alarmInfo.getVehiclePlateNumber()).append("的驾驶员[").append(driverName)
                                    .append("]触动报警开关;");
                            try {
                                int r = alarmOperateService.addAlarmOperate(alarm);
                                //生成操作日志
                                if (r > 0) {
                                        TaxiAlarmLog alarmLog = new TaxiAlarmLog();
                                        alarmLog.setAlarmId(alarmUuid);
                                        alarmLog.setOperationLog(operationLogStr.toString());//添加操作日志
                                        alarmLog.setDeviceId(deviceId);
                                        alarmLog.setStartTime(DateUtil.getYMDHMSFormat(alarmInfo.getStartTime()));//报警开始时间
                                        alarmLog.setLongitude(alarmInfo.getStartLongitude().toString());
                                        alarmLog.setLatitude(alarmInfo.getStartLatitude().toString());
                                        alarmLog.setSpeed(alarmInfo.getStartSpeed().toString());
                                        alarmLog.setDirection(alarmInfo.getDirection());//方向
                                        alarmLogService.insertAlarmLog(alarmLog);
                                }
                            } catch (Exception e) {
                                log.debug("kafka消费类 新增警情处理消息异常" + e.getMessage());
                            }
                        }
                    }
                }
                // 调用接口把缓存中的数据推送至前端
                List<TaxiAlarmInfo> alarmList = redisTemplate.boundListOps("alarmList1").range(0, -1);
                log.debug("推送至前端的弹窗预警数据List:" + alarmList.toString());
                Set<String> clients = getClient(ALARMEVENT, ALARMMONITOR);
                MessgeBean bean = new MessgeBean();
                bean.setContent(alarmList);
                bean.setType(ALARMMONITOR_PUSH);
                pushData(bean.getType(), bean, clients, "/alarm");
            }
        }

    }


    /**
     * 查询当前车辆打卡驾驶员信息
     */
    protected String getWorkInVehicleId(String vehicleId) throws Exception {
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
