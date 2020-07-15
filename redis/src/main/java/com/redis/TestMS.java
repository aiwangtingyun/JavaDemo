package com.redis;

import redis.clients.jedis.Jedis;

public class TestMS {

    public static void main(String[] args) {
        // 测试主从复制
        Jedis jedis_m = new Jedis("127.0.0.1", 6379);
        Jedis jedis_s = new Jedis("127.0.0.1", 6380);

        jedis_s.slaveof("127.0.0.1", 6379);

        jedis_m.set("km", "vm");
        System.out.println(jedis_s.get("km"));
    }
}
