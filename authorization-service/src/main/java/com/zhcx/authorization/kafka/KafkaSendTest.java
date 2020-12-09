package com.zhcx.authorization.kafka;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: qzq
 * @date 2020-07-03 09:32
 **/

@RestController
@RequestMapping("/kafkaSendTest")
public class KafkaSendTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @Autowired
    private TaxiAlarmConsumer taxiAlarmConsumer;

    @GetMapping("")
    public String kafkaSendTest(){
        Map map =new HashMap<>();
        map.put("DEVICE_ID","698e8cd0-9bb0-11e9-9c6a-91ecf8e6b751");
        map.put("ALARM_VAL","1");
        map.put("ALARM_FLAG","1");
        map.put("TIME",String.valueOf(new Date().getTime()));
        map.put("LONGITUDE","1");
        map.put("LATITUDE","1");
        map.put("SPEED","1");
        map.put("DIRECTION","1");
        String jsonString = JSON.toJSONString(map);
//        String deviceId = kafkaObj.getString("DEVICE_ID");//获取的车辆deviceId
//        String alarmVal = kafkaObj.getString("ALARM_VAL");//报警状态标示 0:正常 1:报警
//        String alarmFlag = kafkaObj.getString("ALARM_FLAG");//报警类型 1:紧急报警 2:预警 3:卫星定位模块发生故障 4:...
//        String startTime = kafkaObj.getString("TIME");//报警开始时间 毫秒值
//        String longitude = kafkaObj.getString("LONGITUDE");//报警开始经度
//        String latitude = kafkaObj.getString("LATITUDE");//报警开始纬度
//        String speed = kafkaObj.getString("SPEED");//报警开始速度
//        String direction = kafkaObj.getString("DIRECTION");//方向
        taxiAlarmConsumer.alarmConsume(jsonString);

        System.out.println("kafkaSendTest...........");
        return "成功";
    }
}
