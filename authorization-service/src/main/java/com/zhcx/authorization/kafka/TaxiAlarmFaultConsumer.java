package com.zhcx.authorization.kafka;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhcx.authorization.utils.Constants;
import com.zhcx.authorization.utils.HttpUtils;
import com.zhcx.basicdata.facade.taxi.*;
import com.zhcx.basicdata.pojo.taxi.TaxiAlarmInfo;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoDriver;
import com.zhcx.basicdata.pojo.taxi.TaxiBaseInfoVehicle;
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

/**
 * @description:
 * @author: qiuziqiang
 * @date 2019-07-29 16:28
 **/
@Component
public class TaxiAlarmFaultConsumer {
    @Autowired
    private TaxiAlarmInfoService alarmInfoService;

    @Autowired
    private TaxiBaseInfoVehicleService vehicleService;

    @Autowired
    private TaxiBaseInfoDriverService driverService;

    @Autowired
    private UUIDUtils uuidUtils;

    @Autowired
    private HttpUtils httpUtils;

    @Resource
    private RedisTemplate redisTemplate;

    private static final Logger log = LoggerFactory.getLogger(TaxiAlarmConsumer.class);

    @KafkaListener(topics = "${alarm.fault.consumer.topic}")
    public void alarmFaultConsume(String msgContent) {
        log.debug("alarmFaultConsume开始消费来自kafka的GPS消息(预警/报警信息)" + msgContent);
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

        //其他类型的告警消息在这里处理
        //消息为预警
        if (StringUtils.equals(Constants.ALARM_VAL_YES, alarmVal) && !StringUtils.equals("1", alarmFlag) && !StringUtils.equals("0", alarmFlag)) {
            //1.先查询redis
            Object obj = redisTemplate.opsForValue().get(deviceId + alarmFlag + alarmVal + "yes");
            if (obj != null) {
                return;
            }
            //2.不存在redis中,组装kafka预警对象 alarmInfo
            TaxiAlarmInfo alarmInfo = new TaxiAlarmInfo();
            alarmInfo.setStartFlag(alarmFlag);//报警开始标示位
            alarmInfo.setStartFlagStatus(alarmVal);//报警开始标示位状态
            alarmInfo.setDeviceId(deviceId);//车辆deviceId
            //根据deviceId查询系统中此车辆
            List<TaxiBaseInfoVehicle> vehicles = vehicleService.selectByDeviceId(deviceId);
            if (vehicles != null && vehicles.size() > 0) {
                TaxiBaseInfoVehicle vehicle = vehicles.get(0);
                String vehicleId = vehicle.getUuid();
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
                alarmInfo.setStartTime(new Date(Long.parseLong(startTime)));//报警时间
                alarmInfo.setStartLongitude(Double.parseDouble(longitude));//经度
                alarmInfo.setStartLatitude(Double.parseDouble(latitude));//纬度
                alarmInfo.setStartSpeed(Double.parseDouble(speed));//速度
                alarmInfo.setDirection(direction);
                int res = alarmInfoService.insertAlarmInfo2(alarmInfo);
                //把报警类型为预警的消息加入到缓存
                if (res > 0) {
                    redisTemplate.opsForValue().set(deviceId + alarmFlag + alarmVal + "yes", deviceId + alarmFlag + alarmVal + "yes");
                }
            }
        }
        //消息为解除
        if (StringUtils.equals(Constants.ALARM_VAL_NO, alarmVal) && !StringUtils.equals("1", alarmFlag) && !StringUtils.equals("0", alarmFlag)) {

            //1.先查询redis
            Object obj = redisTemplate.opsForValue().get(deviceId + alarmFlag + Constants.ALARM_VAL_YES + "yes");
            if (obj == null) {
                return;
            }
            //2.已存在报警消息 ,组装解除的 kafka预警对象 alarmInfo
            TaxiAlarmInfo alarmInfo = new TaxiAlarmInfo();
            alarmInfo.setStartFlag(alarmFlag);//报警开始标示位
            alarmInfo.setStartFlagStatus(alarmVal);//报警开始标示位状态
            alarmInfo.setDeviceId(deviceId);//车辆deviceId
            List<TaxiBaseInfoVehicle> vehicles = vehicleService.selectByDeviceId(deviceId);
            if (vehicles.size() > 0) {
                TaxiBaseInfoVehicle vehicle = vehicles.get(0);
                String vehicleId = vehicle.getUuid();
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
                alarmInfo.setStartTime(new Date(Long.parseLong(startTime)));//报警时间
                alarmInfo.setStartLongitude(Double.parseDouble(longitude));//经度
                alarmInfo.setStartLatitude(Double.parseDouble(latitude));//纬度
                alarmInfo.setStartSpeed(Double.parseDouble(speed));//速度
                alarmInfo.setDirection(direction);
                int res = alarmInfoService.insertAlarmInfo2(alarmInfo);
                //3.删除redis中存在的报警消息
                if (res > 0) {
                    redisTemplate.delete(deviceId + alarmFlag + Constants.ALARM_VAL_YES + "yes");
                }
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
