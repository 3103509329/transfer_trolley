package com.car.carservice.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * @ClassName：KafkaProducer
 * @Description:
 * @author：李亮
 * @date：2020/8/1316:04
 */
public class ConsumerTest {

    public static final String USER_SCHEMA = "{\"name\":\"jdbctest\",\"type\":\"record\",\"fields\":[{\"name\":\"ORDERID\",\"type\":\"string\"}]}";

    // public static final String topic="topic-demo";
    public static final String topic = "test_put_cassandra";


    public static final String groupId = "group.demo";


    public static void main(String[] args) {


        //设置消费组的名称
        //将属性值反序列化
        Properties properties = new Properties();
        properties.put("key.deserializer", StringDeserializer.class);
        properties.put("value.deserializer", StringDeserializer.class);
        properties.put("group.id", groupId);
        properties.put("bootstrap.servers", "127.0.0.1:9092");
        //创建一个消费者客户端实例
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        //订阅主题
        consumer.subscribe(Collections.singletonList(topic));

        //循环消费消息
        while (true){
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord<String,String> record : records){
                System.out.println("key:" + record.key() + "" + ",value:" + record.value());
            }
        }
    }
}

