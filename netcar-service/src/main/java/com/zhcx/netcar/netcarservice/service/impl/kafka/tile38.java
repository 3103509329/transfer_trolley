package com.zhcx.netcar.netcarservice.service.impl.kafka;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandType;

/**
 * @ClassName：tile38
 * @Description:
 * @author：李亮
 * @date：2020/10/2016:49
 */
public class tile38 {


    public static void main(String[] args) {
        RedisClient client = RedisClient.create("redis://172.16.102.157:9851");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisCommands<String, String> sync = connection.sync();

        StringCodec codec = StringCodec.UTF8;
        sync.dispatch(CommandType.SET,
                new StatusOutput<>(codec), new CommandArgs<>(codec)
                        .add("fleet")
                        .add("truck1")
                        .add("POINT")
                        .add(33L)
                        .add(-115L));

        String result = sync.dispatch(CommandType.GET,
                new StatusOutput<>(codec), new CommandArgs<>(codec)
                        .add("fleet")
                        .add("truck1"));
        System.out.print("返回结果：" + result);
    }
}
