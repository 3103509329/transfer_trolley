package com.zhcx.netcar.netcarservice.service.impl.kafka;

import com.alibaba.fastjson.JSON;
import com.zhcx.netcar.netcarservice.constant.CassandraEntityTypeEnum;
import com.zhcx.netcar.netcarservice.utils.DateUtil;
import com.zhcx.netcar.pojo.base.NetcarPositionDriver;
import com.zhcx.netcar.pojo.base.NetcarPositionVehicle;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/6/10 0010 14:32
 **/
@Component
public class DriverCassandraStreamServiceImpl implements KafkaStreamService {
    private static final Logger log = LoggerFactory.getLogger(DriverCassandraStreamServiceImpl.class);
    private KafkaStreams streams = null;

    @Value("${netcar.cassandra.driver}")
    private String STREAM_TOPIC_IN;

    @Value("${netcar.cassandra.driver.out}")
    private String STREAM_TOPIC_OUT;

    /**
     * 服务应用标识 唯一
     */
    private final String SERVICE_APP_ID = getClass().getSimpleName();


    @Override
    public void start(final String bootstrapServers, final String stateDir) {
        streams = startKStreams(bootstrapServers, stateDir);
        log.info("Started Service " + getClass().getSimpleName());
    }

    private KafkaStreams startKStreams(final String bootstrapServers, final String stateDir) {
        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> source = builder.stream(STREAM_TOPIC_IN);
        KStream<String, String> max = source.mapValues(value -> {
            Map<String, Object> map = new HashMap<>();
            try {
                NetcarPositionDriver driver = JSON.parseObject(value, NetcarPositionDriver.class);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sdf.format(new Date(Long.parseLong(driver.getPositionTime()) * 1000));
                Date data = DateUtil.getYMDHMSFormat(date);
                map.put("entity_type", CassandraEntityTypeEnum.DRIVER.getCode());
                map.put("entity_id", driver.getLicenseId());
                map.put("key", "FULL");
                map.put("str_v", JSON.toJSONString(driver));
                map.put("ts", data.getTime());
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
            return JSON.toJSONString(map);
        });
        max.to(STREAM_TOPIC_OUT);

        final Properties props = KafkaServiceUtil.baseStreamsConfig(bootstrapServers, stateDir, SERVICE_APP_ID);

        final KafkaStreams streams = new KafkaStreams(builder.build(), props);
        final CountDownLatch startLatch = new CountDownLatch(1);
        streams.setStateListener((newState, oldState) -> {
            if (newState == KafkaStreams.State.RUNNING && oldState == KafkaStreams.State.REBALANCING) {
                startLatch.countDown();
            }

        });
        streams.start();

        try {
            if (!startLatch.await(60, TimeUnit.SECONDS)) {
                throw new RuntimeException("Streams never finished rebalancing on startup");
            }
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return streams;
    }

    @Override
    public void stop() {
        if (streams != null) {
            streams.close();
        }
    }
}
