package com.car.carservice.service.kafka;

import com.alibaba.fastjson.JSON;
import com.car.carservice.pojo.BaseDictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName：VehileCassandraConsumer
 * @Description:
 * @author：李亮
 * @date：2020/7/2117:46
 */
@Component
public class DemoConsumer {
    private static final Logger log = LoggerFactory.getLogger(DemoConsumer.class);

    @KafkaListener(topics = "test_put_cassandra")
    public void alarmConsume(String msgContent) {

        try {
            BaseDictionary baseDictionary = JSON.parseObject(msgContent, BaseDictionary.class);
            if (baseDictionary == null) {
                return;
            }
            log.info("接收数据" + JSON.toJSONString(baseDictionary));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("接收数据异常：" + e.getMessage());
        }
    }
}
