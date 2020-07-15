package com.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TestPool {

    @Test
    public void testPool() {
        JedisPool pool = JedisPoolUtils.getJedisPoolInstance();

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set("pool1", "poolv2");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // jedis 3.0 版本之后使用 close() 而不是 retrunResource() 来释放资源
            if (null != jedis) {
                jedis.close();
            }
        }
    }
}
