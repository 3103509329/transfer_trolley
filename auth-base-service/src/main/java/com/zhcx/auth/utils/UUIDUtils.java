package com.zhcx.auth.utils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import java.util.UUID;

/**
 * @description:
 * @author: qzq
 * @date 2020-04-15 14:37
 **/
public class UUIDUtils {

    private JedisSentinelPool jedisSentinelPool;

    private int uuidRedisDb = 0;

    private String uuidRedisKey = "seq:urbantraffic:uuid";


    public UUIDUtils(JedisSentinelPool jedisSentinelPool) {
        this.jedisSentinelPool = jedisSentinelPool;
    }

    public UUIDUtils(JedisSentinelPool jedisSentinelPool, int uuidRedisDb, String uuidRedisKey) {
        this(jedisSentinelPool);
        this.uuidRedisDb = uuidRedisDb;
        this.uuidRedisKey = uuidRedisKey;
    }

    public Long getLongUUID() {
        Long uuid = null;
        Jedis jedis = null;
        try {
            jedis = jedisSentinelPool.getResource();
            jedis.select(uuidRedisDb);
            uuid = jedis.incr(uuidRedisKey);
            returnResource(jedis);
        } catch (Exception e) {
            returnBrokenResource(jedis);
        }
        return uuid;
    }

    public Long getLongUUID(String key) {
        Long uuid = null;
        Jedis jedis = null;
        try {
            jedis = this.jedisSentinelPool.getResource();
            jedis.select(uuidRedisDb);
            uuid = jedis.incr(key);
            this.returnResource(jedis);
        } catch (Exception e) {
            this.returnBrokenResource(jedis);
        }
        return uuid;
    }

    public String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedisSentinelPool.returnResource(jedis);
        }
    }

    private void returnBrokenResource(Jedis jedis) {
        if (jedis != null) {
            jedisSentinelPool.returnBrokenResource(jedis);
        }
    }

}
