package com.zhcx.netcar.netcarservice.service.impl.kafka;

import com.alibaba.fastjson.JSON;
import com.zhcx.netcar.netcarservice.utils.DateTimeUtil;
import com.zhcx.netcar.pojo.base.NetcarOrderCancel;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/7/9 0009 10:10
 **/
@Component
public class RevocationStreamServiceImpl implements KafkaStreamService {
    private static final Logger log = LoggerFactory.getLogger(RevocationStreamServiceImpl.class);

    @Value("${netcar.stream.topic.revocation.in}")
    private String STREAM_TOPIC_IN;

    @Value("${netcar.stream.topic.revocation.out}")
    private String STREAM_TOPIC_OUT;

    private KafkaStreams streams = null;

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
            System.out.println("获得数据 ： " + value);
            Map<String, Object> map = new HashMap<>();
            try {
                NetcarOrderCancel cancel = JSON.parseObject(value, NetcarOrderCancel.class);
                Date payTime = DateTimeUtil.StringToDate(cancel.getCancelTime(), "yyyyMMddHHmmss");
                map.put("COMPANY_ID", cancel.getCompanyId());
                map.put("ADDRESS", cancel.getAddress());
                map.put("REVOCATION_TYPE", cancel.getCancelTypeCode());
                map.put("REVOCATION_TIME", payTime.getTime());
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
