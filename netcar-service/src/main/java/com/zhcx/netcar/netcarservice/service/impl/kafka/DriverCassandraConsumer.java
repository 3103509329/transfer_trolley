package com.zhcx.netcar.netcarservice.service.impl.kafka;

import com.alibaba.fastjson.JSON;
import com.zhcx.netcar.netcarservice.mapper.cassandra.PositionRepository;
import com.zhcx.netcar.netcarservice.utils.DateTimeUtil;
import com.zhcx.netcar.pojo.cassandra.ts_kv_cf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName：VehileCassandraConsumer
 * @Description:
 * @author：李亮
 * @date：2020/7/2117:46
 */
@Component
public class DriverCassandraConsumer {
    private static final Logger log = LoggerFactory.getLogger(DriverCassandraConsumer.class);
    @Autowired
    private PositionRepository positionRepository;

    @KafkaListener(topics = "${netcar.cassandra.driver.out}")
    public void alarmConsume(String msgContent) {


        try {
            ts_kv_cf ts_kv_cf = JSON.parseObject(msgContent, ts_kv_cf.class);
            if (ts_kv_cf == null) {
                return;
            }
            Long partition = DateTimeUtil.getCurrentMonthPartitionTime(ts_kv_cf.getTs(), null);
            //历史轨迹数据新增
            positionRepository.insertCF(ts_kv_cf.getEntity_type(), ts_kv_cf.getEntity_id(), ts_kv_cf.getKey(), partition, ts_kv_cf.getTs(), ts_kv_cf.getStr_v());
            log.info("驾驶员轨迹数据入库完成: " + "时间" + ts_kv_cf.getTs());
            //实时定位数据维护（先删除已有的数据，新增新数据）
            positionRepository.deleteLatestCF(ts_kv_cf.getEntity_type(), ts_kv_cf.getEntity_id(), partition, ts_kv_cf.getTs());
            log.info("驾驶员实时定位数据维护完成");
            positionRepository.insertLatestCF(ts_kv_cf.getEntity_type(), ts_kv_cf.getEntity_id(), ts_kv_cf.getKey(), ts_kv_cf.getStr_v(), ts_kv_cf.getTs());
            log.info("驾驶员实时定位数据新增完成");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("驾驶员定位入库canssandra异常：" + String.valueOf(e.getMessage()));
        }
    }
}
