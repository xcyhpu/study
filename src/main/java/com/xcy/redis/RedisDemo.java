package com.xcy.redis;

import redis.clients.jedis.Jedis;

import java.util.Random;
import java.util.UUID;

/**
 * Created by xuchunyang on 2018/6/11 14点05分
 */
public class RedisDemo {


    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    public static void main(String[] args) {


        /** 尝试获取分布式锁 */

        final Jedis jedis = JedisUtil.getWriteJedisUtil().getJedisPool().getResource();

        String lockKey = "redis:distribute:key:businessName:";

        String requestId = UUID.randomUUID().toString();

        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, 15000);

        assert LOCK_SUCCESS.equals(result);

    }

}
