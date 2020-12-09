package com.zhcx.netcar.netcarservice.service.impl.kafka;

import com.alibaba.fastjson.JSON;
import com.zhcx.netcar.netcarservice.utils.DateTimeUtil;
import com.zhcx.netcar.pojo.base.NetcarOperatePay;
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
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/6/9 9:42
 *
 * 基于订单数据统计，可用于计算: 运距分布
 *
 **/
@Component
public class DistanceStreamServiceImpl implements KafkaStreamService {
    private static final Logger log = LoggerFactory.getLogger(DistanceStreamServiceImpl.class);
    private KafkaStreams streams = null;

    @Value("${netcar.stream.topic.distance.in}")
    private String STREAM_TOPIC_IN;

    @Value("${netcar.stream.topic.distance.out}")
    private String STREAM_TOPIC_OUT;

    /**
     * 服务应用标识 唯一
     */
    private final String SERVICE_APP_ID = getClass().getSimpleName();

    public DistanceStreamServiceImpl() {
    }

    @Override
    public void start(final String bootstrapServers, final String stateDir) {
        streams = startKStreams(bootstrapServers, stateDir);
        log.info("Started Service " + getClass().getSimpleName());
        System.out.print("基于订单数据统计，可用于计算: 运距分布");
    }

    /**
     * type 1：运距在3公里以内
     * type 2：运距在3到5公里
     * type 3: 运距在5到8公里
     * type 4: 运距在8到10公里
     * type 5: 运距在10到20公里
     * type 6: 运距在20公里以上
     * @param bootstrapServers
     * @param stateDir
     * @return
     */
    private KafkaStreams startKStreams(final String bootstrapServers, final String stateDir) {
        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> source = builder.stream(STREAM_TOPIC_IN);
        KStream<String, String> max = source.mapValues(value -> {
            Map<String, Object> map = new HashMap<>();
            String newType = "";
            try{
                NetcarOperatePay order = JSON.parseObject(value, NetcarOperatePay.class);
                if (order.getDriveMile() < 3) {
                    newType = "1";
                } else if (order.getDriveMile() >= 3 && order.getDriveMile() < 5) {
                    newType = "2";
                } else if (order.getDriveMile() >= 5 && order.getDriveMile() < 8) {
                    newType = "3";
                } else if (order.getDriveMile() >= 8 && order.getDriveMile() < 10) {
                    newType = "4";
                } else if (order.getDriveMile() >= 10 && order.getDriveMile() < 20) {
                    newType = "5";
                } else {
                    newType = "6";
                }
                Date payTime = DateTimeUtil.StringToDate(order.getOrderMatchTime(),"yyyyMMddHHmmss");
                map.put("PAY_TIME", payTime.getTime());
                map.put("COMPANY_ID", order.getCompanyId());
                map.put("HANDLING_TYPE", newType);
                map.put("drive_mile", order.getDriveMile());
                map.put("wait_mile", order.getWaitMile());
            }catch (Exception e){
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
