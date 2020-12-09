package com.zhcx.netcar.netcarservice.service.impl.kafka;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/6/9 9:41
 **/
public interface KafkaStreamService {

    void start(String bootstrapServers, String stateDir);

    void stop();
}
