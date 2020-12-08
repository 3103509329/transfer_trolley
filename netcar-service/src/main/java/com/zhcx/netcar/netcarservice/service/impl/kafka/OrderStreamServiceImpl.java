package com.zhcx.netcar.netcarservice.service.impl.kafka;

import com.alibaba.fastjson.JSON;
import com.zhcx.netcar.netcarservice.utils.DateTimeUtil;
import com.zhcx.netcar.pojo.base.NetcarOperatePay;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
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
 * 基于订单数据统计，可用于计算：
 * 1.营运趟次
 * 2.营运金额
 * 3.营运里程
 * 3.公里产值
 * 4.平均空驶时间
 * 5.平均空驶里程
 * 6.平均营运时间
 * 7.平均营运里程
 * 8.车单趟平均营收金额
 * 9.车日均营运趟次
 *
 **/
@Component
public class OrderStreamServiceImpl implements KafkaStreamService {
    private static final Logger log = LoggerFactory.getLogger(OrderStreamServiceImpl.class);
    private KafkaStreams streams = null;

    @Value("${netcar.stream.topic.order.in}")
    private String STREAM_TOPIC_IN;

    @Value("${netcar.stream.topic.order.out}")
    private String STREAM_TOPIC_OUT;


    /**
     * 服务应用标识 唯一
     */
    private final String SERVICE_APP_ID = getClass().getSimpleName();

    public OrderStreamServiceImpl() {

    }

    @Override
    public void start(String bootstrapServers, final String stateDir) {
        streams = startKStreams(bootstrapServers, stateDir);
        log.info("Started Service " + getClass().getSimpleName());
    }

    private KafkaStreams startKStreams(final String bootstrapServers, final String stateDir) {
        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> source = builder.stream(STREAM_TOPIC_IN);
        KStream<String, String> kStream = source.mapValues(value ->{
            Map<String, Object> map = new HashMap<>();
            try{
                NetcarOperatePay order = JSON.parseObject(value, NetcarOperatePay.class);
                //发送到druid
                Date payTime = DateTimeUtil.StringToDate(order.getOrderMatchTime(),"yyyyMMddHHmmss");
                //订单完成时间
                map.put("PAY_TIME", payTime.getTime());
                map.put("COMPANY_ID", order.getCompanyId());
                map.put("VEHICLE_NO", order.getVehicleNo());
                map.put("ON_AREA", order.getOnArea());
                map.put("fact_price", order.getFactPrice());
                map.put("wait_time", order.getWaitTime());
                map.put("wait_mile", String.valueOf(order.getWaitMile()));
                map.put("drive_time", order.getDriveTime());
                map.put("drive_mile", String.valueOf(order.getDriveMile()));
            }catch (Exception e){
                e.printStackTrace();
                return "";
            }
            return JSON.toJSONString(map);
        });
        kStream.peek((key, value) -> {

        });
        kStream.to(STREAM_TOPIC_OUT);

        final Properties props = KafkaServiceUtil.baseStreamsConfig(bootstrapServers, stateDir, SERVICE_APP_ID);
        Topology topology = builder.build();
        System.out.println(topology.describe());
        final KafkaStreams streams = new KafkaStreams(topology, props);
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



    public static void main(final String[] args) throws Exception {

        final String bootstrapServers = "taxi1.123cx.com:9092";

        final OrderStreamServiceImpl service = new OrderStreamServiceImpl();
        service.start(bootstrapServers, "/tmp/kafka-streams");
        KafkaServiceUtil.addShutdownHookAndBlock(service);
    }
}
